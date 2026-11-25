package pe.edu.upc.demosi.dtos;

public class ObjetoCategoriaDTCList {
    private Long idObjeto;
    private String nombreObjeto;
    private String descripcionObjeto;
    private String colorObjeto;
    private String marcaObjeto;
    private Long idCategoria;
    private String nombreCategoria;

    public Long getIdObjeto() { return idObjeto; }
    public void setIdObjeto(Long idObjeto) { this.idObjeto = idObjeto; }

    public String getNombreObjeto() { return nombreObjeto; }
    public void setNombreObjeto(String nombreObjeto) { this.nombreObjeto = nombreObjeto; }

    public String getDescripcionObjeto() { return descripcionObjeto; }
    public void setDescripcionObjeto(String descripcionObjeto) { this.descripcionObjeto = descripcionObjeto; }

    public String getColorObjeto() { return colorObjeto; }
    public void setColorObjeto(String colorObjeto) { this.colorObjeto = colorObjeto; }

    public String getMarcaObjeto() { return marcaObjeto; }
    public void setMarcaObjeto(String marcaObjeto) { this.marcaObjeto = marcaObjeto; }

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
}