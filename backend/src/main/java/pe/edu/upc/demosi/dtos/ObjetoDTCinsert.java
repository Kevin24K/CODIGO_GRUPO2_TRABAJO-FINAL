package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ObjetoDTCinsert {
    private Long idObjeto;

    @NotBlank(message = "El nombre del objeto no puede ser nulo ni estar vacío")
    private String nombreObjeto;

    @NotBlank(message = "La descripción del objeto no puede ser nula ni estar vacía")
    private String descripcionObjeto;

    @NotBlank(message = "El color del objeto no puede ser nulo ni estar vacío")
    private String colorObjeto;

    @NotBlank(message = "La marca del objeto no puede ser nula ni estar vacía")
    private String marcaObjeto;

    @NotNull(message = "El estado del objeto no puede ser nulo ni estar vacío")
    private Boolean activoObjeto;

    @NotNull(message = "El Id de la Categoría es obligatorio.")
    private Long idCategoria;

    public Long getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Long idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public String getDescripcionObjeto() {
        return descripcionObjeto;
    }

    public void setDescripcionObjeto(String descripcionObjeto) {
        this.descripcionObjeto = descripcionObjeto;
    }

    public String getColorObjeto() {
        return colorObjeto;
    }

    public void setColorObjeto(String colorObjeto) {
        this.colorObjeto = colorObjeto;
    }

    public String getMarcaObjeto() {
        return marcaObjeto;
    }

    public void setMarcaObjeto(String marcaObjeto) {
        this.marcaObjeto = marcaObjeto;
    }

    public Boolean getActivoObjeto() {
        return activoObjeto;
    }

    public void setActivoObjeto(Boolean activoObjeto) {
        this.activoObjeto = activoObjeto;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }
}
