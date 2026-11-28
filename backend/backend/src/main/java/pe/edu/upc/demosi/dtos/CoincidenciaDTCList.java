package pe.edu.upc.demosi.dtos;

import java.time.LocalDateTime;

public class CoincidenciaDTCList {

    private Long idCoincidencia;
    private Double porcentajeSimilitudC;
    private String detalleC;
    private LocalDateTime fechaGeneracionC;

    public Long getIdCoincidencia() {
        return idCoincidencia;
    }

    public void setIdCoincidencia(Long idCoincidencia) {
        this.idCoincidencia = idCoincidencia;
    }

    public Double getPorcentajeSimilitudC() {
        return porcentajeSimilitudC;
    }

    public void setPorcentajeSimilitudC(Double porcentajeSimilitudC) {
        this.porcentajeSimilitudC = porcentajeSimilitudC;
    }

    public String getDetalleC() {
        return detalleC;
    }

    public void setDetalleC(String detalleC) {
        this.detalleC = detalleC;
    }

    public LocalDateTime getFechaGeneracionC() {
        return fechaGeneracionC;
    }

    public void setFechaGeneracionC(LocalDateTime fechaGeneracionC) {
        this.fechaGeneracionC = fechaGeneracionC;
    }
}
