package pe.edu.upc.demosi.servicesimplements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.*;
import pe.edu.upc.demosi.servicesinterfaces.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoincidenciaAutomaticaServicesImplement implements ICoincidenciaAutomaticaService {

    private static final Logger log = LoggerFactory.getLogger(CoincidenciaAutomaticaServicesImplement.class);
    private static final String PERDIDO = "PERDIDO";
    private static final String ENCONTRADO = "ENCONTRADO";

    private final IReporteService rS;
    private final IIaService iaS;
    private final ICoincidenciaService cS;
    private final IEstadoCoincidenciaService ecS;
    private final INotificacionService nS;
    private final IPushService pS; // Firebase Cloud Messaging

    // % mínimo para avisar al usuario (application.properties)
    @Value("${coincidencias.umbral:80}")
    private double umbral;

    public CoincidenciaAutomaticaServicesImplement(IReporteService rS, IIaService iaS, ICoincidenciaService cS,
                                                   IEstadoCoincidenciaService ecS, INotificacionService nS,
                                                   IPushService pS) {
        this.pS = pS;
        this.rS = rS;
        this.iaS = iaS;
        this.cS = cS;
        this.ecS = ecS;
        this.nS = nS;
    }

    @Override
    public int procesarReporte(Reporte nuevo) {
        if (nuevo == null || nuevo.getObjeto() == null || !Boolean.TRUE.equals(nuevo.getActivo())) {
            return 0;
        }
        boolean esPerdido = PERDIDO.equalsIgnoreCase(nuevo.getTipoReporte());
        boolean esEncontrado = ENCONTRADO.equalsIgnoreCase(nuevo.getTipoReporte());
        if (!esPerdido && !esEncontrado) {
            return 0;
        }

        // Si el reporte es PERDIDO se compara con los ENCONTRADOS, y al revés
        String tipoContrario = esPerdido ? ENCONTRADO : PERDIDO;
        List<Reporte> candidatos = rS.listActivos().stream()
                .filter(r -> tipoContrario.equalsIgnoreCase(r.getTipoReporte()))
                .filter(r -> r.getObjeto() != null)
                .filter(r -> !r.getIdReporte().equals(nuevo.getIdReporte()))
                .toList();
        if (candidatos.isEmpty()) {
            return 0;
        }

        List<Objeto> objetos = candidatos.stream().map(Reporte::getObjeto).toList();
        IIaService.Resultado res = iaS.compararObjetos(nuevo.getObjeto(), objetos, esPerdido);

        int creadas = 0;
        for (int i = 0; i < candidatos.size(); i++) {
            double porcentaje = res.porcentajes()[i];
            if (porcentaje < umbral) continue;

            Reporte perdido = esPerdido ? nuevo : candidatos.get(i);
            Reporte encontrado = esPerdido ? candidatos.get(i) : nuevo;
            if (yaExiste(perdido, encontrado)) continue;

            Coincidencia c = crearCoincidencia(perdido, encontrado, porcentaje, res.metodo());
            notificar(perdido, encontrado, c, porcentaje);
            creadas++;
        }
        if (creadas > 0) {
            log.info("Reporte {}: {} coincidencia(s) nuevas con similitud >= {} % ({})",
                    nuevo.getIdReporte(), creadas, umbral, res.metodo());
        }
        return creadas;
    }

    @Override
    public int procesarObjeto(Long idObjeto) {
        int total = 0;
        List<Reporte> reportes = rS.listActivos().stream()
                .filter(r -> r.getObjeto() != null && idObjeto.equals(r.getObjeto().getIdObjeto()))
                .toList();
        for (Reporte r : reportes) {
            total += procesarReporte(r);
        }
        return total;
    }

    // No repetir la misma pareja perdido-encontrado
    private boolean yaExiste(Reporte perdido, Reporte encontrado) {
        return cS.list().stream().anyMatch(c ->
                c.getReportePerdido() != null && c.getReporteEncontrado() != null
                        && perdido.getIdReporte().equals(c.getReportePerdido().getIdReporte())
                        && encontrado.getIdReporte().equals(c.getReporteEncontrado().getIdReporte()));
    }

    private Coincidencia crearCoincidencia(Reporte perdido, Reporte encontrado, double porcentaje, String metodo) {
        Coincidencia c = new Coincidencia();
        c.setReportePerdido(perdido);
        c.setReporteEncontrado(encontrado);
        c.setPorcentajeSimilitudC(porcentaje);
        c.setDetalleC("Generada automáticamente con " + metodo + ": \"" + perdido.getObjeto().getNombreObjeto()
                + "\" (perdido) vs \"" + encontrado.getObjeto().getNombreObjeto() + "\" (encontrado)");
        c.setFechaGeneracionC(LocalDateTime.now());
        c.setEstadoCoincidencia(estadoPendiente());
        cS.insert(c);
        return c;
    }

    private void notificar(Reporte perdido, Reporte encontrado, Coincidencia c, double porcentaje) {
        String nombreEncontrado = encontrado.getObjeto().getNombreObjeto();
        String nombrePerdido = perdido.getObjeto().getNombreObjeto();

        Notificacion n = new Notificacion();
        n.setUsuario(perdido.getUsuario());
        n.setCoincidencia(c);
        n.setTipoNotificacion("COINCIDENCIA");
        n.setLeidaNotificacion(false);
        n.setFechaCreacionN(LocalDateTime.now());
        n.setTituloNotificacion(corto("Posible coincidencia: " + nombreEncontrado, 100));
        n.setMensajeNotificacion(corto("Encontramos \"" + nombreEncontrado + "\" con " + formato(porcentaje)
                + " % de similitud con tu reporte \"" + nombrePerdido
                + "\". Puede ser tu objeto: revisa la foto y acércate a la oficina de objetos perdidos.", 255));
        nS.insert(n);

        // Además del aviso en la campanita, se manda un push con Firebase
        try {
            pS.enviar(perdido.getUsuario(), n.getTituloNotificacion(), n.getMensajeNotificacion(), n.getIdNotificacion());
        } catch (Exception e) {
            log.warn("No se pudo enviar el push: {}", e.getMessage());
        }
    }

    // Usa el estado PENDIENTE; si no existe, lo crea
    private EstadoCoincidencia estadoPendiente() {
        return ecS.list().stream()
                .filter(e -> "PENDIENTE".equalsIgnoreCase(e.getNombreEC()))
                .findFirst()
                .orElseGet(() -> {
                    EstadoCoincidencia e = new EstadoCoincidencia();
                    e.setNombreEC("PENDIENTE");
                    e.setDescripcionEC("Coincidencia sugerida por el sistema, pendiente de revisión");
                    ecS.insert(e);
                    return e;
                });
    }

    private String formato(double p) {
        return p == Math.floor(p) ? String.valueOf((long) p) : String.valueOf(p);
    }

    private String corto(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
