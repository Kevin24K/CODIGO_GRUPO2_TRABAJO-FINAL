package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.Usuario;

// Notificaciones push con Firebase Cloud Messaging
public interface IPushService {

    // Guarda (o reasigna) el token FCM del navegador del usuario
    void registrarToken(Usuario usuario, String token);

    void eliminarToken(String token);

    // Envía la notificación a todos los dispositivos del usuario. Devuelve cuántos la recibieron.
    int enviar(Usuario usuario, String titulo, String mensaje, Long idNotificacion);

    boolean activo();
}
