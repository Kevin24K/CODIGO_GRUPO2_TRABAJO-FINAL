package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    @Column(name = "tipo_reporte", length = 50, nullable = false)
    private String tipoReporte;

    @Column(name = "fecha_evento_reporte", nullable = false)
    private LocalDate fechaeventoR;

    @Column(name = "hora_evento_reporte")
    private LocalTime horaEventoR;

    @Column(name = "fecha_creacion_reporte", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion_reporte")
    private LocalDateTime fechaActualizacionR;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true; // Le ponemos true para que al crearse sea activo por defecto

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_objeto", nullable = false)
    private Objeto objeto;

    @ManyToOne
    @JoinColumn(name = "id_estado_reporte", nullable = false)
    private EstadoReporte estadoReporte;

    @OneToMany(mappedBy = "reportePerdido")
    private List<Coincidencia> coincidenciasComoPerdido;

    // 2. Lista de coincidencias donde este reporte es el objeto ENCONTRADO
    @OneToMany(mappedBy = "reporteEncontrado")
    private List<Coincidencia> coincidenciasComoEncontrado;

    public Reporte() {
    }

    public Reporte(Long idReporte, String tipoReporte, LocalDate fechaeventoR, LocalTime horaEventoR, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacionR, Boolean activo, Usuario usuario, Objeto objeto, EstadoReporte estadoReporte, List<Coincidencia> coincidenciasComoPerdido, List<Coincidencia> coincidenciasComoEncontrado) {
        this.idReporte = idReporte;
        this.tipoReporte = tipoReporte;
        this.fechaeventoR = fechaeventoR;
        this.horaEventoR = horaEventoR;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacionR = fechaActualizacionR;
        this.activo = activo;
        this.usuario = usuario;
        this.objeto = objeto;
        this.estadoReporte = estadoReporte;
        this.coincidenciasComoPerdido = coincidenciasComoPerdido;
        this.coincidenciasComoEncontrado = coincidenciasComoEncontrado;
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public LocalDate getFechaeventoR() {
        return fechaeventoR;
    }

    public void setFechaeventoR(LocalDate fechaeventoR) {
        this.fechaeventoR = fechaeventoR;
    }

    public LocalTime getHoraEventoR() {
        return horaEventoR;
    }

    public void setHoraEventoR(LocalTime horaEventoR) {
        this.horaEventoR = horaEventoR;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacionR() {
        return fechaActualizacionR;
    }

    public void setFechaActualizacionR(LocalDateTime fechaActualizacionR) {
        this.fechaActualizacionR = fechaActualizacionR;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public EstadoReporte getEstadoReporte() {
        return estadoReporte;
    }

    public void setEstadoReporte(EstadoReporte estadoReporte) {
        this.estadoReporte = estadoReporte;
    }

    public List<Coincidencia> getCoincidenciasComoPerdido() {
        return coincidenciasComoPerdido;
    }

    public void setCoincidenciasComoPerdido(List<Coincidencia> coincidenciasComoPerdido) {
        this.coincidenciasComoPerdido = coincidenciasComoPerdido;
    }

    public List<Coincidencia> getCoincidenciasComoEncontrado() {
        return coincidenciasComoEncontrado;
    }

    public void setCoincidenciasComoEncontrado(List<Coincidencia> coincidenciasComoEncontrado) {
        this.coincidenciasComoEncontrado = coincidenciasComoEncontrado;
    }
}
