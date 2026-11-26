package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReporteDTCinsert {

    private Long idReporte;

    @NotBlank(message = "El tipo de reporte no puede ser nulo ni estar vacío")
    private String tipoReporte;

    @NotNull(message = "La fecha del evento no puede ser nula ni estar vacía")
    private LocalDate fechaeventoR;

    @NotNull(message = "La hora del evento no puede ser nula ni estar vacía")
    private LocalTime horaEventoR;

    @NotNull(message = "La fecha de creación no puede ser nula ni estar vacía")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "La fecha de actualización no puede ser nula ni estar vacía")
    private LocalDateTime fechaActualizacionR;

    @NotNull(message = "El Id del Usuario es obligatorio.")
    private Long idUsuario;

    @NotNull(message = "El Id del Objeto es obligatorio.")
    private Long idObjeto;

    @NotNull(message = "El Id del Estado del Reporte es obligatorio.")
    private Long idEstadoReporte;

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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Long idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Long getIdEstadoReporte() {
        return idEstadoReporte;
    }

    public void setIdEstadoReporte(Long idEstadoReporte) {
        this.idEstadoReporte = idEstadoReporte;
    }
}
