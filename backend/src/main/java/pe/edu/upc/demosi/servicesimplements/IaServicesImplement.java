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

    @Value("${huggingface.token:}")
    private String token;

    @Value("${huggingface.url:https://router.huggingface.co}")
    private String baseUrl;

    @Value("${huggingface.modelo-vision:Qwen/Qwen2.5-VL-7B-Instruct}")
    private String modeloVision;

    @Value("${huggingface.modelo-embeddings:sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2}")
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
            // Formato OpenAI-compatible del router de Hugging Face (modelo de visión)
            ObjectNode body = mapper.createObjectNode();
            body.put("model", modeloVision);
            body.put("max_tokens", 90);
            ArrayNode messages = body.putArray("messages");
            ObjectNode msg = messages.addObject();
            msg.put("role", "user");
            ArrayNode content = msg.putArray("content");
            content.addObject()
                    .put("type", "text")
                    .put("text", "Eres el asistente de una oficina de objetos perdidos. "
                            + "Describe en español, en una sola oración de máximo 25 palabras, el objeto principal "
                            + "de la foto: qué es, color, marca visible y rasgos distintivos. No describas el fondo.");
            ObjectNode img = content.addObject();
            img.put("type", "image_url");
            img.putObject("image_url").put("url", fotoBase64);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/chat/completions"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                log.warn("Hugging Face (visión) respondió {}: {}", resp.statusCode(), resp.body());
                return null;
            }
            String texto = mapper.readTree(resp.body())
                    .path("choices").path(0).path("message").path("content").asText("").trim();
            return texto.isEmpty() ? null : texto;
        } catch (Exception e) {
            log.warn("No se pudo describir la imagen con Hugging Face: {}", e.getMessage());
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
            // 1) Hugging Face convierte cada texto en un vector (embedding)
            // 2) Smile calcula la similitud coseno entre el texto buscado y cada objeto
            double[] consulta = vectores.get(0);
            puntajes = new double[objetos.size()];
            for (int i = 0; i < objetos.size(); i++) {
                puntajes[i] = Math.max(0, MathEx.cos(consulta, vectores.get(i + 1)));
            }
            metodo = "IA (Hugging Face + Smile)";
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

    // Llama a Hugging Face (feature-extraction). Posición 0 = texto buscado.
    private List<double[]> embeddings(String consulta, List<String> textos) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            List<String> todos = new ArrayList<>();
            todos.add(consulta);
            todos.addAll(textos);

            ObjectNode body = mapper.createObjectNode();
            ArrayNode inputs = body.putArray("inputs");
            todos.forEach(inputs::add);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/hf-inference/models/" + modeloEmbeddings + "/pipeline/feature-extraction"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                log.warn("Hugging Face (embeddings) respondió {}: {}", resp.statusCode(), resp.body());
                return null;
            }
            JsonNode json = mapper.readTree(resp.body());
            List<double[]> vectores = new ArrayList<>();
            for (JsonNode item : json) {
                vectores.add(aVector(item));
            }
            return vectores.size() == todos.size() ? vectores : null;
        } catch (Exception e) {
            log.warn("No se pudieron obtener embeddings de Hugging Face: {}", e.getMessage());
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
