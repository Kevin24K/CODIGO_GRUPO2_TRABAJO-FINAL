package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NotificacionDTOUpdate {
    private Long idNotificacion;

    @NotBlank(message = "El título de la notificación no puede ser nulo ni estar vacío")
    private String tituloNotificacion;

    @NotBlank(message = "El mensaje de la notificación no puede ser nulo ni estar vacío")
    private String mensajeNotificacion;

    @NotBlank(message = "El tipo de notificación no puede ser nulo ni estar vacío")
    private String tipoNotificacion;

    @NotNull(message = "El estado de la notificación no puede ser nulo ni estar vacío")
    private Boolean leidaNotificacion;

    @NotNull(message = "La fecha de creación de la notificación no puede ser nula ni estar vacía")
    private LocalDateTime fechaCreacionN;

    @NotNull(message = "El ID del usuario no puede ser nulo ni estar vacío")
    private Long idUsuario;

    @NotNull(message = "El ID de la coincidencia no puede ser nulo ni estar vacío")
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