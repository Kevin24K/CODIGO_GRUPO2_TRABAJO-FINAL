package pe.edu.upc.demosi.dtos;

import pe.edu.upc.demosi.entities.Categoria;

public class ObjetoDTCList {

    private Long idObjeto;
    private String nombreObjeto;
    private String descripcionObjeto;
    private String colorObjeto;
    private String marcaObjeto;
    private Boolean activoObjeto;
    private String fotoObjeto;
    private String descripcionIaObjeto;
    private Categoria categoria;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getFotoObjeto() {
        return fotoObjeto;
    }

    public void setFotoObjeto(String fotoObjeto) {
        this.fotoObjeto = fotoObjeto;
    }

    public String getDescripcionIaObjeto() {
        return descripcionIaObjeto;
    }

    public void setDescripcionIaObjeto(String descripcionIaObjeto) {
        this.descripcionIaObjeto = descripcionIaObjeto;
    }
}
