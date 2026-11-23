package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UsuarioDTOUpdate {
    private Long idUsuario;

    @NotBlank(message = "El nombre del usuario no puede ser nulo ni estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido del usuario no puede ser nulo ni estar vacío")
    private String apellido;

    @NotBlank(message = "El correo del usuario no puede ser nulo ni estar vacío")
    private String correo;

    @NotNull(message = "La fecha de registro del usuario no puede ser nula ni estar vacía")
    private LocalDateTime fechaRegistro;

    @NotNull(message = "El estado del usuario no puede ser nulo ni estar vacío")
    private boolean activo;

    @NotNull(message = "El ID del rol no puede ser nulo ni estar vacío")
    private Long idRol;

    @NotBlank(message = "El nombre del rol no puede ser nulo ni estar vacío")
    private String nombreRol;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}