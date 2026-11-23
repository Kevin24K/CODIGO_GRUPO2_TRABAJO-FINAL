package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDTCinsert {

    @NotBlank(message = "El nombre no puede ser nulo ni estar vacío")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede ser nulo ni estar vacío")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
    private String apellido;

    @NotBlank(message = "El correo no puede ser nulo ni estar vacío")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña no puede ser nula ni estar vacía")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String contrasena;

    @NotNull(message = "El Id del Rol es obligatorio.")
    private Long idRol;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }
}