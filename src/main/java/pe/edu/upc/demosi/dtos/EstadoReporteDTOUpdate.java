package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

public class EstadoReporteDTOUpdate {
    private Long idEstadoReporte;

    @NotBlank(message = "El nombre del estado del reporte no puede ser nulo ni estar vacío")
    private String nombreEC;

    @NotBlank(message = "La descripción del estado del reporte no puede ser nulo ni estar vacío")
    private String descripcionEC;

    public Long getIdEstadoReporte() {
        return idEstadoReporte;
    }

    public void setIdEstadoReporte(Long idEstadoReporte) {
        this.idEstadoReporte = idEstadoReporte;
    }

    public String getNombreEC() {
        return nombreEC;
    }

    public void setNombreEC(String nombreEC) {
        this.nombreEC = nombreEC;
    }

    public String getDescripcionEC() {
        return descripcionEC;
    }

    public void setDescripcionEC(String descripcionEC) {
        this.descripcionEC = descripcionEC;
    }
}