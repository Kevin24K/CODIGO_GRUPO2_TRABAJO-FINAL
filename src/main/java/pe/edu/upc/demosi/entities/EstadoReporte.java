package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;


@Entity
@Table(name = "Estado_Reporte")
public class EstadoReporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadoReporte;

    @Column(name = "nombreEReporte", length = 50, nullable = false)
    private String nombreEReporte;

    @Column(name = "descripcionEReporte", length = 255)
    private String descripcionEReporte;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true; // true por defecto al crearse

    public EstadoReporte() {
    }

    public EstadoReporte(Long idEstadoReporte, String nombreEReporte, String descripcionEReporte, Boolean activo) {
        this.idEstadoReporte = idEstadoReporte;
        this.nombreEReporte = nombreEReporte;
        this.descripcionEReporte = descripcionEReporte;
        this.activo = activo;
    }

    public Long getIdEstadoReporte() {
        return idEstadoReporte;
    }

    public void setIdEstadoReporte(Long idEstadoReporte) {
        this.idEstadoReporte = idEstadoReporte;
    }

    public String getNombreEReporte() {
        return nombreEReporte;
    }

    public void setNombreEReporte(String nombreEReporte) {
        this.nombreEReporte = nombreEReporte;
    }

    public String getDescripcionEReporte() {
        return descripcionEReporte;
    }

    public void setDescripcionEReporte(String descripcionEReporte) {
        this.descripcionEReporte = descripcionEReporte;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
