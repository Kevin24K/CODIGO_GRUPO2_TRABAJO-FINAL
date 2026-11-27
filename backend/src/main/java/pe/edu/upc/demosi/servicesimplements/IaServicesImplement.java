package pe.edu.upc.demosi.servicesimplements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.dtos.ObjetoSimilarDTO;
import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.servicesinterfaces.IIaService;
import pe.edu.upc.demosi.servicesinterfaces.IObjetoService;
import smile.math.MathEx;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Normalizer;
import java.time.Duration;
import java.util.*;

@Service
public class IaServicesImplement implements IIaService {

    private static final Logger log = LoggerFactory.getLogger(IaServicesImplement.class);

    // Palabras que no aportan significado (para el modo sin IA)
    private static final Set<String> STOPWORDS = Set.of(
            "el", "la", "los", "las", "un", "una", "unos", "unas", "de", "del", "al", "y", "o", "con",
            "sin", "en", "por", "para", "que", "mi", "mis", "su", "sus", "es", "son", "se", "lo",
            "le", "a", "color", "marca", "categoria", "perdi", "perdido", "perdida", "busco", "estoy");

    private final IObjetoService oS;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    // ===== Gemini (Google AI Studio) =====
    @Value("${gemini.api-key:}")
    private String token;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta}")
    private String baseUrl;

    @Value("${gemini.modelo-vision:gemini-3.5-flash-lite}")
    private String modeloVision;

    @Value("${gemini.modelo-embeddings:gemini-embedding-001}")
    private String modeloEmbeddings;

    public IaServicesImplement(IObjetoService oS) {
        this.oS = oS;
    }

    // ================= HUF19: descripción de la foto =================

    @Override
    public String describirImagen(String fotoBase64) {
        if (fotoBase64 == null || fotoBase64.isBlank() || token == null || token.isBlank()) {
            return null; // sin foto o sin token: no se genera descripción
        }
        try {
            // La foto llega como "data:image/jpeg;base64,AAAA..." -> separar tipo y datos
            String mime = "image/jpeg";
            String datos = fotoBase64;
            int coma = fotoBase64.indexOf(',');
            if (fotoBase64.startsWith("data:") && coma > 0) {
                mime = fotoBase64.substring(5, fotoBase64.indexOf(';'));
                datos = fotoBase64.substring(coma + 1);
            }

            // Gemini generateContent: imagen + instrucción en un solo mensaje
            ObjectNode body = mapper.createObjectNode();
            ArrayNode parts = body.putArray("contents").addObject().putArray("parts");
            ObjectNode inline = parts.addObject().putObject("inline_data");
            inline.put("mime_type", mime);
            inline.put("data", datos);
            parts.addObject().put("text", "Eres el asistente de una oficina de objetos perdidos. "
                    + "Describe en español, en una sola oración de máximo 25 palabras, el objeto principal "
                    + "de la foto: qué es, color, marca visible y rasgos distintivos. No describas el fondo. "
                    + "Responde solo con la oración.");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/models/" + modeloVision + ":generateContent"))
                    .timeout(Duration.ofSeconds(60))
                    .header("x-goog-api-key", token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                log.warn("Gemini (visión) respondió {}: {}", resp.statusCode(), resp.body());
                return null;
            }
            // Respuesta: candidates[0].content.parts[*].text
            StringBuilder texto = new StringBuilder();
            for (JsonNode part : mapper.readTree(resp.body()).path("candidates").path(0).path("content").path("parts")) {
                texto.append(part.path("text").asText(""));
            }
            String r = texto.toString().trim();
            return r.isEmpty() ? null : r;
        } catch (Exception e) {
            log.warn("No se pudo describir la imagen con Gemini: {}", e.getMessage());
            return null;
        }
    }

    // ================= HUF17: búsqueda por similitud =================

    @Override
    public List<ObjetoSimilarDTO> buscarSimilares(String texto, int top) {
        List<Objeto> objetos = oS.list().stream()
                .filter(o -> Boolean.TRUE.equals(o.isActivoObjeto()))
                .toList();
        if (objetos.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> textosObjetos = objetos.stream().map(this::textoDelObjeto).toList();

        double[] puntajes;
        String metodo;
        List<double[]> vectores = embeddings(texto, textosObjetos);
        if (vectores != null) {
            // 1) Gemini convierte cada texto en un vector (embedding)
            // 2) Smile calcula la similitud coseno entre el texto buscado y cada objeto
            double[] consulta = vectores.get(0);
            puntajes = new double[objetos.size()];
            for (int i = 0; i < objetos.size(); i++) {
                puntajes[i] = Math.max(0, MathEx.cos(consulta, vectores.get(i + 1)));
            }
            metodo = "IA (Gemini + Smile)";
        } else {
            // Respaldo si no hay token o falla la API: % de palabras buscadas que aparecen en el objeto
            puntajes = new double[objetos.size()];
            Set<String> q = tokens(texto);
            for (int i = 0; i < objetos.size(); i++) {
                puntajes[i] = cobertura(q, tokens(textosObjetos.get(i)));
            }
            metodo = "Texto (sin IA)";
        }

        List<ObjetoSimilarDTO> resultado = new ArrayList<>();
        for (int i = 0; i < objetos.size(); i++) {
            resultado.add(aDTO(objetos.get(i), puntajes[i], metodo));
        }
        resultado.sort(Comparator.comparingDouble(ObjetoSimilarDTO::getPorcentaje).reversed());
        return resultado.subList(0, Math.min(top, resultado.size()));
    }

    // ================= Notificaciones: coincidencias automáticas =================

    @Override
    public Resultado compararObjetos(Objeto ancla, List<Objeto> otros, boolean anclaEsPerdido) {
        double[] porcentajes = new double[otros.size()];
        if (otros.isEmpty()) {
            return new Resultado(porcentajes, "Texto (sin IA)");
        }
        String textoAncla = textoDelObjeto(ancla);
        List<String> textos = otros.stream().map(this::textoDelObjeto).toList();

        List<double[]> vectores = embeddings(textoAncla, textos);
        if (vectores != null) {
            for (int i = 0; i < otros.size(); i++) {
                double c = Math.max(0, MathEx.cos(vectores.get(0), vectores.get(i + 1)));
                porcentajes[i] = Math.round(c * 1000.0) / 10.0;
            }
            return new Resultado(porcentajes, "IA (Gemini + Smile)");
        }

        // Sin IA: % de palabras del objeto PERDIDO que aparecen en el ENCONTRADO
        Set<String> tAncla = tokens(textoAncla);
        for (int i = 0; i < otros.size(); i++) {
            Set<String> tOtro = tokens(textos.get(i));
            double c = anclaEsPerdido ? cobertura(tAncla, tOtro) : cobertura(tOtro, tAncla);
            porcentajes[i] = Math.round(c * 1000.0) / 10.0;
        }
        return new Resultado(porcentajes, "Texto (sin IA)");
    }

    // Todo lo que describe al objeto en un solo texto
    private String textoDelObjeto(Objeto o) {
        StringBuilder sb = new StringBuilder();
        sb.append(nn(o.getNombreObjeto())).append(". ");
        sb.append(nn(o.getDescripcionObjeto())).append(". ");
        if (o.getDescripcionIaObjeto() != null) sb.append(o.getDescripcionIaObjeto()).append(". ");
        sb.append("Color ").append(nn(o.getColorObjeto())).append(". ");
        sb.append("Marca ").append(nn(o.getMarcaObjeto())).append(". ");
        if (o.getCategoria() != null) sb.append("Categoría ").append(nn(o.getCategoria().getNombreCategoria()));
        return sb.toString();
    }

    // Llama a Gemini (batchEmbedContents). Posición 0 = texto buscado.
    private List<double[]> embeddings(String consulta, List<String> textos) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            List<String> todos = new ArrayList<>();
            todos.add(consulta);
            todos.addAll(textos);

            List<double[]> vectores = new ArrayList<>();
            // Gemini acepta hasta 100 textos por llamada
            for (int desde = 0; desde < todos.size(); desde += 100) {
                List<String> lote = todos.subList(desde, Math.min(desde + 100, todos.size()));
                ObjectNode body = mapper.createObjectNode();
                ArrayNode requests = body.putArray("requests");
                for (String t : lote) {
                    ObjectNode r = requests.addObject();
                    r.put("model", "models/" + modeloEmbeddings);
                    r.putObject("content").putArray("parts").addObject().put("text", t);
                }

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl + "/models/" + modeloEmbeddings + ":batchEmbedContents"))
                        .timeout(Duration.ofSeconds(60))
                        .header("x-goog-api-key", token)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                        .build();

                HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() != 200) {
                    log.warn("Gemini (embeddings) respondió {}: {}", resp.statusCode(), resp.body());
                    return null;
                }
                // Respuesta: { "embeddings": [ { "values": [ ... ] }, ... ] }
                for (JsonNode e : mapper.readTree(resp.body()).path("embeddings")) {
                    vectores.add(aVector(e.path("values")));
                }
            }
            return vectores.size() == todos.size() ? vectores : null;
        } catch (Exception e) {
            log.warn("No se pudieron obtener embeddings de Gemini: {}", e.getMessage());
            return null;
        }
    }

    // Acepta [n] (ya promediado) o [tokens][n] (se promedia aquí)
    private double[] aVector(JsonNode item) {
        if (item.size() > 0 && item.get(0).isArray()) {
            int dim = item.get(0).size();
            double[] v = new double[dim];
            for (JsonNode fila : item) {
                for (int j = 0; j < dim; j++) v[j] += fila.get(j).asDouble();
            }
            for (int j = 0; j < dim; j++) v[j] /= item.size();
            return v;
        }
        double[] v = new double[item.size()];
        for (int j = 0; j < item.size(); j++) v[j] = item.get(j).asDouble();
        return v;
    }

    private ObjetoSimilarDTO aDTO(Objeto o, double puntaje, String metodo) {
        ObjetoSimilarDTO d = new ObjetoSimilarDTO();
        d.setIdObjeto(o.getIdObjeto());
        d.setNombreObjeto(o.getNombreObjeto());
        d.setCategoria(o.getCategoria() != null ? o.getCategoria().getNombreCategoria() : "");
        d.setDescripcionObjeto(o.getDescripcionObjeto());
        d.setDescripcionIaObjeto(o.getDescripcionIaObjeto());
        d.setColorObjeto(o.getColorObjeto());
        d.setMarcaObjeto(o.getMarcaObjeto());
        d.setFotoObjeto(o.getFotoObjeto());
        d.setPorcentaje(Math.round(puntaje * 1000.0) / 10.0); // 0.873 -> 87.3
        d.setMetodo(metodo);
        return d;
    }

    // ----- utilidades del modo sin IA -----
    private Set<String> tokens(String s) {
        String limpio = Normalizer.normalize(nn(s).toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9 ]", " ");
        Set<String> set = new HashSet<>();
        for (String t : limpio.split("\\s+")) {
            if (t.length() > 2 && !STOPWORDS.contains(t)) set.add(raiz(t));
        }
        return set;
    }

    // Quita plural y género para que "negra" y "negros" cuenten igual que "negro"
    private String raiz(String t) {
        if (t.length() > 4 && t.endsWith("es")) t = t.substring(0, t.length() - 2);
        else if (t.length() > 3 && t.endsWith("s")) t = t.substring(0, t.length() - 1);
        if (t.length() > 4 && (t.endsWith("a") || t.endsWith("o"))) t = t.substring(0, t.length() - 1);
        return t;
    }

    private double cobertura(Set<String> buscadas, Set<String> objeto) {
        if (buscadas.isEmpty()) return 0;
        Set<String> comunes = new HashSet<>(buscadas);
        comunes.retainAll(objeto);
        return (double) comunes.size() / buscadas.size();
    }

    private String nn(String s) {
        return s == null ? "" : s;
    }
}
