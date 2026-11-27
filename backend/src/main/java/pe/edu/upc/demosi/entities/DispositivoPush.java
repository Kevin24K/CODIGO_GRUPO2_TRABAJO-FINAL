package pe.edu.upc.demosi.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Notificaciones push (Firebase): cada navegador/celular donde el usuario activó las notificaciones
@Entity
@Table(name = "dispositivo_push")
public class DispositivoPush {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDispositivo;

    // Token que entrega Firebase Cloud Messaging al navegador
    @Column(name = "token_fcm", columnDefinition = "TEXT", nullable = false, unique = true)
    private String tokenFcm;

    @Column(name = "fecha_registro_dispositivo", nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public DispositivoPush() {
    }

    public Long getIdDispositivo() { return idDispositivo; }
    public void setIdDispositivo(Long idDispositivo) { this.idDispositivo = idDispositivo; }
    public String getTokenFcm() { return tokenFcm; }
    public void setTokenFcm(String tokenFcm) { this.tokenFcm = tokenFcm; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
