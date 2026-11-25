package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(name = "nombreCategoria", length = 100, nullable = false)
    private String nombreCategoria;

    @Column(name = "descripcionCategoria", length = 255)
    private String descripcionCategoria;

    @Column(name = "activoCategoria", nullable = false)
    private Boolean activoCategoria;

    public Categoria() {
    }

    public Categoria(Long idCategoria, String nombreCategoria, String descripcionCategoria, Boolean  activoCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
        this.activoCategoria = activoCategoria;
    }

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

    public Boolean getActivoCategoria() {
        return activoCategoria;
    }

    public void setActivoCategoria(Boolean activoCategoria) {
        this.activoCategoria = activoCategoria;
    }
}
