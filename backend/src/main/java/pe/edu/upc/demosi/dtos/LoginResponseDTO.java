package pe.edu.upc.demosi.dtos;

public class LoginResponseDTO {
    private String token;
    private String correo;

    public LoginResponseDTO(String token, String correo) {
        this.token = token;
        this.correo = correo;
    }

    public String getToken() { return token; }
    public String getCorreo() { return correo; }
}