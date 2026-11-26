package pe.edu.upc.demosi.dtos;

public class ReporteListDTO {
    private Long idReporte;
    private String tipoReporte;
    private String fechaEvento;
    private String estadoReporte;
    private Long idUsuario;
    private Long idObjeto;

    public ReporteListDTO() {}
    public Long getIdReporte() { return idReporte; }
    public void setIdReporte(Long idReporte) { this.idReporte = idReporte; }
    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }
    public String getEstadoReporte() { return estadoReporte; }
    public void setEstadoReporte(String estadoReporte) { this.estadoReporte = estadoReporte; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdObjeto() { return idObjeto; }
    public void setIdObjeto(Long idObjeto) { this.idObjeto = idObjeto; }
}
