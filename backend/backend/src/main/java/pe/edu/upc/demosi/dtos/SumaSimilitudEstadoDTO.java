package pe.edu.upc.demosi.dtos;

public class SumaSimilitudEstadoDTO {
    private String nombreEstadoCoincidencia;
    private Double sumaSimilitud;

    public SumaSimilitudEstadoDTO() {
    }

    public SumaSimilitudEstadoDTO(String nombreEstadoCoincidencia, Double sumaSimilitud) {
        this.nombreEstadoCoincidencia = nombreEstadoCoincidencia;
        this.sumaSimilitud = sumaSimilitud;
    }

    public String getNombreEstadoCoincidencia() {
        return nombreEstadoCoincidencia;
    }

    public void setNombreEstadoCoincidencia(String nombreEstadoCoincidencia) {
        this.nombreEstadoCoincidencia = nombreEstadoCoincidencia;
    }

    public Double getSumaSimilitud() {
        return sumaSimilitud;
    }

    public void setSumaSimilitud(Double sumaSimilitud) {
        this.sumaSimilitud = sumaSimilitud;
    }
}
