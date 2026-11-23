package pe.edu.upc.demosi.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReporteDTCList {
    private Long idReporte;
    private String tipoReporte;
    private LocalDate fechaeventoR;
    private LocalTime horaEventoR;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacionR;
    private Long idUsuario;
    private Long idObjeto;
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
