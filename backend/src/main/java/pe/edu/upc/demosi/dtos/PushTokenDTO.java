package pe.edu.upc.demosi.dtos;

import jakarta.validation.constraints.NotBlank;

// Notificaciones push: token FCM que manda el navegador
public class PushTokenDTO {
    @NotBlank
    private String token;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
