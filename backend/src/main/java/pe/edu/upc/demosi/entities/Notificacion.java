package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;

    @Column(name = "tituloNotificacion", length = 100, nullable = false)
    private String tituloNotificacion;

    @Column(name = "mensajeNotificacion", length = 255, nullable = false)
    private String mensajeNotificacion;

    @Column(name = "tipoNotificacion", length = 50)
    private String tipoNotificacion;

    @Column(name = "leidaNotificacion", nullable = false)
    private Boolean leidaNotificacion;

    @Column(name = "fecha_creacion_notificacion", nullable = false)
    private LocalDateTime fechaCreacionN;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_coincidencia") // Puede ser nullable si hay notificaciones genéricas del sistema
    private Coincidencia coincidencia;

    public Notificacion() {
    }

    public Notificacion(Long idNotificacion, String tituloNotificacion, String mensajeNotificacion, String tipoNotificacion, Boolean leidaNotificacion, LocalDateTime fechaCreacionN, Usuario usuario, Coincidencia coincidencia) {
        this.idNotificacion = idNotificacion;
        this.tituloNotificacion = tituloNotificacion;
        this.mensajeNotificacion = mensajeNotificacion;
        this.tipoNotificacion = tipoNotificacion;
        this.leidaNotificacion = leidaNotificacion;
        this.fechaCreacionN = fechaCreacionN;
        this.usuario = usuario;
        this.coincidencia = coincidencia;
    }

    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTituloNotificacion() {
        return tituloNotificacion;
    }

    public void setTituloNotificacion(String tituloNotificacion) {
        this.tituloNotificacion = tituloNotificacion;
    }

    public String getMensajeNotificacion() {
        return mensajeNotificacion;
    }

    public void setMensajeNotificacion(String mensajeNotificacion) {
        this.mensajeNotificacion = mensajeNotificacion;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Boolean getLeidaNotificacion() {
        return leidaNotificacion;
    }

    public void setLeidaNotificacion(Boolean leidaNotificacion) {
        this.leidaNotificacion = leidaNotificacion;
    }

    public LocalDateTime getFechaCreacionN() {
        return fechaCreacionN;
    }

    public void setFechaCreacionN(LocalDateTime fechaCreacionN) {
        this.fechaCreacionN = fechaCreacionN;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Coincidencia getCoincidencia() {
        return coincidencia;
    }

    public void setCoincidencia(Coincidencia coincidencia) {
        this.coincidencia = coincidencia;
    }
}
