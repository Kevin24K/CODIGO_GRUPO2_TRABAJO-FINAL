package pe.edu.upc.demosi.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Notificaciones: lo que ve el usuario en su bandeja (notificación + objeto encontrado)
public class NotificacionDetalleDTO {
    private Long idNotificacion;
    private String tituloNotificacion;
    private String mensajeNotificacion;
    private String tipoNotificacion;
    private Boolean leidaNotificacion;
    private LocalDateTime fechaCreacionN;
    private Long idCoincidencia;
    private Double porcentaje;
    private String estadoCoincidencia;
    private Long idReportePerdido;
    private String objetoPerdido;
    private Long idReporteEncontrado;
    private Long idObjetoEncontrado;
    private String objetoEncontrado;
    private String categoriaEncontrado;
    private String descripcionEncontrado;
    private String descripcionIaEncontrado;
    private String colorEncontrado;
    private String marcaEncontrado;
    private String fotoEncontrado;
    private LocalDate fechaEncontrado;

    public Long getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Long idNotificacion) { this.idNotificacion = idNotificacion; }
    public String getTituloNotificacion() { return tituloNotificacion; }
    public void setTituloNotificacion(String tituloNotificacion) { this.tituloNotificacion = tituloNotificacion; }
    public String getMensajeNotificacion() { return mensajeNotificacion; }
    public void setMensajeNotificacion(String mensajeNotificacion) { this.mensajeNotificacion = mensajeNotificacion; }
    public String getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(String tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }
    public Boolean getLeidaNotificacion() { return leidaNotificacion; }
    public void setLeidaNotificacion(Boolean leidaNotificacion) { this.leidaNotificacion = leidaNotificacion; }
    public LocalDateTime getFechaCreacionN() { return fechaCreacionN; }
    public void setFechaCreacionN(LocalDateTime fechaCreacionN) { this.fechaCreacionN = fechaCreacionN; }
    public Long getIdCoincidencia() { return idCoincidencia; }
    public void setIdCoincidencia(Long idCoincidencia) { this.idCoincidencia = idCoincidencia; }
    public Double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Double porcentaje) { this.porcentaje = porcentaje; }
    public String getEstadoCoincidencia() { return estadoCoincidencia; }
    public void setEstadoCoincidencia(String estadoCoincidencia) { this.estadoCoincidencia = estadoCoincidencia; }
    public Long getIdReportePerdido() { return idReportePerdido; }
    public void setIdReportePerdido(Long idReportePerdido) { this.idReportePerdido = idReportePerdido; }
    public String getObjetoPerdido() { return objetoPerdido; }
    public void setObjetoPerdido(String objetoPerdido) { this.objetoPerdido = objetoPerdido; }
    public Long getIdReporteEncontrado() { return idReporteEncontrado; }
    public void setIdReporteEncontrado(Long idReporteEncontrado) { this.idReporteEncontrado = idReporteEncontrado; }
    public Long getIdObjetoEncontrado() { return idObjetoEncontrado; }
    public void setIdObjetoEncontrado(Long idObjetoEncontrado) { this.idObjetoEncontrado = idObjetoEncontrado; }
    public String getObjetoEncontrado() { return objetoEncontrado; }
    public void setObjetoEncontrado(String objetoEncontrado) { this.objetoEncontrado = objetoEncontrado; }
    public String getCategoriaEncontrado() { return categoriaEncontrado; }
    public void setCategoriaEncontrado(String categoriaEncontrado) { this.categoriaEncontrado = categoriaEncontrado; }
    public String getDescripcionEncontrado() { return descripcionEncontrado; }
    public void setDescripcionEncontrado(String descripcionEncontrado) { this.descripcionEncontrado = descripcionEncontrado; }
    public String getDescripcionIaEncontrado() { return descripcionIaEncontrado; }
    public void setDescripcionIaEncontrado(String descripcionIaEncontrado) { this.descripcionIaEncontrado = descripcionIaEncontrado; }
    public String getColorEncontrado() { return colorEncontrado; }
    public void setColorEncontrado(String colorEncontrado) { this.colorEncontrado = colorEncontrado; }
    public String getMarcaEncontrado() { return marcaEncontrado; }
    public void setMarcaEncontrado(String marcaEncontrado) { this.marcaEncontrado = marcaEncontrado; }
    public String getFotoEncontrado() { return fotoEncontrado; }
    public void setFotoEncontrado(String fotoEncontrado) { this.fotoEncontrado = fotoEncontrado; }
    public LocalDate getFechaEncontrado() { return fechaEncontrado; }
    public void setFechaEncontrado(LocalDate fechaEncontrado) { this.fechaEncontrado = fechaEncontrado; }
}
