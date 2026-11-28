package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CoincidenciaDTCinsert {

    private Long idCoincidencia;

    @NotNull(message = "El ID del reporte perdido es obligatorio")
    private Long idReportePerdido;

    @NotNull(message = "El ID del reporte encontrado es obligatorio")
    private Long idReporteEncontrado;

    @NotNull(message = "El ID del estado es obligatorio")
    private Long idEstadoCoincidencia;

    @NotNull(message = "El porcentaje de similitud es obligatorio")
    private Double porcentajeSimilitudC;

    @NotBlank(message = "El detalle de la coincidencia es obligatorio")
    private String detalleC;

    @NotNull(message = "La fecha de generación es obligatoria")
    private LocalDateTime fechaGeneracionC;

    public Long getIdCoincidencia() {
        return idCoincidencia;
    }

    public void setIdCoincidencia(Long idCoincidencia) {
        this.idCoincidencia = idCoincidencia;
    }

    public Long getIdReportePerdido() {
        return idReportePerdido;
    }

    public void setIdReportePerdido(Long idReportePerdido) {
        this.idReportePerdido = idReportePerdido;
    }

    public Long getIdReporteEncontrado() {
        return idReporteEncontrado;
    }

    public void setIdReporteEncontrado(Long idReporteEncontrado) {
        this.idReporteEncontrado = idReporteEncontrado;
    }

    public Long getIdEstadoCoincidencia() {
        return idEstadoCoincidencia;
    }

    public void setIdEstadoCoincidencia(Long idEstadoCoincidencia) {
        this.idEstadoCoincidencia = idEstadoCoincidencia;
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
}
