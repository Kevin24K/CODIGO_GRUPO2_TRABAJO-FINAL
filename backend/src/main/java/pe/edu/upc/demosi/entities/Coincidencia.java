package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Coincidencia")
public class Coincidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCoincidencia;

    @Column(name = "porcentaje_similitud_coincidencia")
    private Double porcentajeSimilitudC;

    @Column(name = "detalle_coincidencia", columnDefinition = "TEXT")
    private String detalleC;

    @Column(name = "fecha_generacion_coincidencia", nullable = false)
    private LocalDateTime fechaGeneracionC;

    @ManyToOne
    @JoinColumn(name = "id_reporte_perdido", nullable = false)
    private Reporte reportePerdido;

    // Conecta con el reporte del objeto que alguien encontró
    @ManyToOne
    @JoinColumn(name = "id_reporte_encontrado", nullable = false)
    private Reporte reporteEncontrado;

    // Conecta con el estado de esta coincidencia
    @ManyToOne
    @JoinColumn(name = "id_estado_coincidencia", nullable = false)
    private EstadoCoincidencia estadoCoincidencia;

    public Coincidencia() {
    }

    public Coincidencia(Long idCoincidencia, Double porcentajeSimilitudC, String detalleC, LocalDateTime fechaGeneracionC, Reporte reportePerdido, Reporte reporteEncontrado, EstadoCoincidencia estadoCoincidencia) {
        this.idCoincidencia = idCoincidencia;
        this.porcentajeSimilitudC = porcentajeSimilitudC;
        this.detalleC = detalleC;
        this.fechaGeneracionC = fechaGeneracionC;
        this.reportePerdido = reportePerdido;
        this.reporteEncontrado = reporteEncontrado;
        this.estadoCoincidencia = estadoCoincidencia;
    }

    public Long getIdCoincidencia() {
        return idCoincidencia;
    }

    public void setIdCoincidencia(Long idCoincidencia) {
        this.idCoincidencia = idCoincidencia;
    }

    public Double getPorcentajeSimilitudC() {
        return porcentajeSimilitudC;
    }

    public void setPorcentajeSimilitudC(Double porcentajeSimilitudC) {
        this.porcentajeSimilitudC = porcentajeSimilitudC;
    }

    public String getDetalleC() {
        return detalleC;
    }

    public void setDetalleC(String detalleC) {
        this.detalleC = detalleC;
    }

    public LocalDateTime getFechaGeneracionC() {
        return fechaGeneracionC;
    }

    public void setFechaGeneracionC(LocalDateTime fechaGeneracionC) {
        this.fechaGeneracionC = fechaGeneracionC;
    }

    public Reporte getReportePerdido() {
        return reportePerdido;
    }

    public void setReportePerdido(Reporte reportePerdido) {
        this.reportePerdido = reportePerdido;
    }

    public Reporte getReporteEncontrado() {
        return reporteEncontrado;
    }

    public void setReporteEncontrado(Reporte reporteEncontrado) {
        this.reporteEncontrado = reporteEncontrado;
    }

    public EstadoCoincidencia getEstadoCoincidencia() {
        return estadoCoincidencia;
    }

    public void setEstadoCoincidencia(EstadoCoincidencia estadoCoincidencia) {
        this.estadoCoincidencia = estadoCoincidencia;
    }
}
