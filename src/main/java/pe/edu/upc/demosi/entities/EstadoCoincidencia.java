package pe.edu.upc.demosi.entities;
import jakarta.persistence.*;


@Entity
@Table(name = "EstadoCoincidencia")
public class EstadoCoincidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadoCoincidencia;

    @Column(name = "nombre_estado_coincidencia", length = 50, nullable = false)
    private String nombreEC;

    @Column(name = "descripcionEstadoCoincidencia", length = 255)
    private String descripcionEC;

    public EstadoCoincidencia() {
    }

    public EstadoCoincidencia(Long idEstadoCoincidencia, String nombreEC, String descripcionEC) {
        this.idEstadoCoincidencia = idEstadoCoincidencia;
        this.nombreEC = nombreEC;
        this.descripcionEC = descripcionEC;
    }

    public Long getIdEstadoCoincidencia() {
        return idEstadoCoincidencia;
    }

    public void setIdEstadoCoincidencia(Long idEstadoCoincidencia) {
        this.idEstadoCoincidencia = idEstadoCoincidencia;
    }

    public String getNombreEC() {
        return nombreEC;
    }

    public void setNombreEC(String nombreEC) {
        this.nombreEC = nombreEC;
    }

    public String getDescripcionEC() {
        return descripcionEC;
    }

    public void setDescripcionEC(String descripcionEC) {
        this.descripcionEC = descripcionEC;
    }
}
