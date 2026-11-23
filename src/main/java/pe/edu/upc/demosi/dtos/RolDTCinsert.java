package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

public class RolDTCinsert {

    private Long idRol;

    @NotBlank(message = "El nombre del rol no puede ser nulo ni estar vacío")
    private String nombreRol;

    @NotBlank(message = "La descripción del rol no puede ser nula ni estar vacía")
    private String descripcionRol;

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }
}
