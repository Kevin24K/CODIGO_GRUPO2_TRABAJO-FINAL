package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriaDTOUpdate {
    @NotBlank(message = "El nombre de la categoría no puede ser nulo ni estar vacío")
    private String nombreCategoria;
    @NotBlank(message = "El descripción de la categoría no puede ser nulo ni estar vacío")
    private String descripcionCategoria;
    @NotNull(message = "El estado de la categoría es obligatorio")
    private Boolean activoCategoria;

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public Boolean isActivoCategoria() {
        return activoCategoria;
    }

    public void setActivoCategoria(Boolean activoCategoria) {
        this.activoCategoria = activoCategoria;
    }
}
