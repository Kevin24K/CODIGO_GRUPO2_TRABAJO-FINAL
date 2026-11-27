package pe.edu.upc.demosi.dtos;

public class CategoriaDTCList {
    private Long idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;
    private Boolean activoCategoria;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

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
