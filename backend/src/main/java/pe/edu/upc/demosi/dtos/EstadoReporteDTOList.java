package pe.edu.upc.demosi.dtos;

public class EstadoReporteDTOList {

    private Long idEstadoReporte;
    private String nombreEReporte;
    private String descripcionEReporte;

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
}
