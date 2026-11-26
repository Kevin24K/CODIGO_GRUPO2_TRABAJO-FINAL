package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReporteInsertDTO {
    @NotNull(message = "El id de usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El id de objeto es obligatorio")
    private Long idObjeto;

    @NotNull(message = "El id de estado es obligatorio")
    private Long idEstadoReporte;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String tipoReporte;

    @NotBlank(message = "La fecha del evento es obligatoria")
    private String fechaEvento;

    private String horaEvento;

    public ReporteInsertDTO() {}
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdObjeto() { return idObjeto; }
    public void setIdObjeto(Long idObjeto) { this.idObjeto = idObjeto; }
    public Long getIdEstadoReporte() { return idEstadoReporte; }
    public void setIdEstadoReporte(Long idEstadoReporte) { this.idEstadoReporte = idEstadoReporte; }
    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }
    public String getHoraEvento() { return horaEvento; }
    public void setHoraEvento(String horaEvento) { this.horaEvento = horaEvento; }
}
