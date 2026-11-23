package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "Objeto")
public class Objeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObjeto;

    @Column(name = "nombre_objeto", length = 100, nullable = false)
    private String nombreObjeto;

    @Column(name = "descripcionObjeto", columnDefinition = "TEXT")
    private String descripcionObjeto;

    @Column(name = "colorObjeto", length = 50)
    private String colorObjeto;

    @Column(name = "marcaObjeto", length = 100)
    private String marcaObjeto;

    @Column(name = "activoObjeto", nullable = false)
    private Boolean activoObjeto;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    public Objeto() {
    }

    public Objeto(Long idObjeto, String nombreObjeto, String descripcionObjeto, String colorObjeto, String marcaObjeto, Boolean activoObjeto, Categoria categoria) {
        this.idObjeto = idObjeto;

        this.nombreObjeto = nombreObjeto;
        this.descripcionObjeto = descripcionObjeto;
        this.colorObjeto = colorObjeto;
        this.marcaObjeto = marcaObjeto;
        this.activoObjeto = activoObjeto;
        this.categoria = categoria;
    }

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

    public Boolean isActivoObjeto() {
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
}
