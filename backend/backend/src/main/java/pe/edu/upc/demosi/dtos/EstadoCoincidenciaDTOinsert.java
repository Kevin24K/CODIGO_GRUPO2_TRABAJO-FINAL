package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

public class EstadoCoincidenciaDTOinsert {

    private Long idEstadoCoincidencia;

    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombreEC;

    @NotBlank(message = "La descripción no puede superar los 255 caracteres")
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
