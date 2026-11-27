package pe.edu.upc.demosi.dtos;

import java.time.LocalDateTime;

public class NotificacionDTCList {

    private Long idNotificacion;
    private String tituloNotificacion;
    private String mensajeNotificacion;
    private String tipoNotificacion;
    private Boolean leidaNotificacion;
    private LocalDateTime fechaCreacionN;
    private Long idUsuario;
    private Long idCoincidencia;

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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdCoincidencia() {
        return idCoincidencia;
    }

    public void setIdCoincidencia(Long idCoincidencia) {
        this.idCoincidencia = idCoincidencia;
    }
}
