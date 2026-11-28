package pe.edu.upc.demosi.dtos;

// HUF17: un objeto encontrado con su % de similitud
public class ObjetoSimilarDTO {

    private Long idObjeto;
    private String nombreObjeto;
    private String categoria;
    private String descripcionObjeto;
    private String descripcionIaObjeto;
    private String colorObjeto;
    private String marcaObjeto;
    private String fotoObjeto;
    private double porcentaje;   // 0 a 100
    private String metodo;       // "IA (Gemini + Smile)" o "Texto (sin IA)"

    public Long getIdObjeto() { return idObjeto; }
    public void setIdObjeto(Long idObjeto) { this.idObjeto = idObjeto; }

    public String getNombreObjeto() { return nombreObjeto; }
    public void setNombreObjeto(String nombreObjeto) { this.nombreObjeto = nombreObjeto; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcionObjeto() { return descripcionObjeto; }
    public void setDescripcionObjeto(String descripcionObjeto) { this.descripcionObjeto = descripcionObjeto; }

    public String getDescripcionIaObjeto() { return descripcionIaObjeto; }
    public void setDescripcionIaObjeto(String descripcionIaObjeto) { this.descripcionIaObjeto = descripcionIaObjeto; }

    public String getColorObjeto() { return colorObjeto; }
    public void setColorObjeto(String colorObjeto) { this.colorObjeto = colorObjeto; }

    public String getMarcaObjeto() { return marcaObjeto; }
    public void setMarcaObjeto(String marcaObjeto) { this.marcaObjeto = marcaObjeto; }

    public String getFotoObjeto() { return fotoObjeto; }
    public void setFotoObjeto(String fotoObjeto) { this.fotoObjeto = fotoObjeto; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
}
