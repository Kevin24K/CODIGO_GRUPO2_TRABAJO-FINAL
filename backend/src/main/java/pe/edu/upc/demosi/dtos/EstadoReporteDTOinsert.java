package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

public class EstadoReporteDTOinsert {

    private Long idEstadoReporte;

    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombreEReporte;

    @NotBlank(message = "La descripción del estado es obligatoria")
    private String descripcionEReporte;

    public Long getIdEstadoReporte() {
        return idEstadoReporte;
    }

    public void setIdEstadoReporte(Long idEstadoReporte) {
        this.idEstadoReporte = idEstadoReporte;
    }

    public String getNombreEReporte() {
        return nombreEReporte;
    }

    public void setNombreEReporte(String nombreEReporte) {
        this.nombreEReporte = nombreEReporte;
    }

    public String getDescripcionEReporte() {
        return descripcionEReporte;
    }

    public void setDescripcionEReporte(String descripcionEReporte) {
        this.descripcionEReporte = descripcionEReporte;
    }
}
