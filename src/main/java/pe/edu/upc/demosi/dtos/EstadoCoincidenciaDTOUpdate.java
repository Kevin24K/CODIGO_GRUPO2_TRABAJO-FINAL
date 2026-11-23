package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

public class EstadoCoincidenciaDTOUpdate {
    private Long idEstadoCoincidencia;

    @NotBlank(message = "El nombre del estado de la coincidencia no puede ser nulo ni estar vacío")
    private String nombreEC;

    @NotBlank(message = "La descripción del estado de la coincidencia no puede ser nulo ni estar vacío")
    private String descripcionEC;

    public Long getIdEstadoCoincidencia() {
        return idEstadoCoincidencia;
    }

    public void setIdEstadoCoincidencia(Long idEstadoCoincidencia) {
        this.idEstadoCoincidencia = idEstadoCoincidencia;
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