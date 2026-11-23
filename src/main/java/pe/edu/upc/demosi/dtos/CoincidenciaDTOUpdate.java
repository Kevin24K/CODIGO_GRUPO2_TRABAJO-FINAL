package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CoincidenciaDTOUpdate {
    private Long idCoincidencia;

    @NotNull(message = "El porcentaje de similitud no puede ser nulo ni estar vacío")
    private Double porcentajeSimilitudC;

    @NotBlank(message = "El detalle de la coincidencia no puede ser nulo ni estar vacío")
    private String detalleC;

    @NotNull(message = "La fecha de generación no puede ser nula ni estar vacía")
    private LocalDateTime fechaGeneracionC;

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

    public Long getIdCoincidencia() {
        return idCoincidencia;
    }

    public void setIdCoincidencia(Long idCoincidencia) {
        this.idCoincidencia = idCoincidencia;
    }
}