package pe.edu.upc.demosi.servicesimplements;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.DispositivoPush;
import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.repositories.IDispositivoPushRepository;
import pe.edu.upc.demosi.servicesinterfaces.IPushService;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class PushServicesImplement implements IPushService {

    private static final Logger log = LoggerFactory.getLogger(PushServicesImplement.class);

    private final IDispositivoPushRepository dR;
    private FirebaseMessaging messaging; // null si Firebase no está

    // Ruta al JSON de la cuenta de servicio de Firebase (variable FIREBASE_CREDENTIALS)
    @Value("${firebase.credenciales:}")
    private String rutaCredenciales;

    public PushServicesImplement(IDispositivoPushRepository dR) {
        this.dR = dR;
    }

    @PostConstruct
    public void iniciar() {
        if (rutaCredenciales == null || rutaCredenciales.isBlank()) {
            log.warn("Firebase no configurado (FIREBASE_CREDENTIALS vacío): no se enviarán notificaciones push");
            return;
        }
        try (InputStream in = new FileInputStream(rutaCredenciales)) {
            FirebaseOptions opciones = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();
            FirebaseApp app = FirebaseApp.getApps().isEmpty()
                    ? FirebaseApp.initializeApp(opciones)
                    : FirebaseApp.getInstance();
            messaging = FirebaseMessaging.getInstance(app);
            log.info("Firebase Cloud Messaging listo");
        } catch (Exception e) {
            log.warn("No se pudo iniciar Firebase: {}", e.getMessage());
        }
    }

    @Override
    public boolean activo() {
        return messaging != null;
    }

    @Override
    public void registrarToken(Usuario usuario, String token) {
        DispositivoPush d = dR.findByTokenFcm(token).orElseGet(DispositivoPush::new);
        d.setTokenFcm(token);
        d.setUsuario(usuario); // si el navegador cambió de usuario, el token pasa al nuevo
        d.setFechaRegistro(LocalDateTime.now());
        dR.save(d);
    }

    @Override
    public void eliminarToken(String token) {
        dR.findByTokenFcm(token).ifPresent(dR::delete);
    }

    @Override
    public int enviar(Usuario usuario, String titulo, String mensaje, Long idNotificacion) {
        if (messaging == null || usuario == null) {
            return 0;
        }
        int enviados = 0;
        for (DispositivoPush d : dR.findByUsuario_IdUsuario(usuario.getIdUsuario())) {
            // Mensaje "data": el service worker del navegador arma la notificación
            Message m = Message.builder()
                    .setToken(d.getTokenFcm())
                    .putData("titulo", titulo)
                    .putData("mensaje", mensaje)
                    .putData("url", "/notificaciones")
                    .putData("idNotificacion", String.valueOf(idNotificacion))
                    .build();
            try {
                messaging.send(m);
                enviados++;
            } catch (FirebaseMessagingException e) {
                // Token vencido o de un navegador que ya no existe: se borra
                if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED
                        || e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                    dR.delete(d);
                } else {
                    log.warn("Firebase no pudo enviar la notificación: {}", e.getMessage());
                }
            }
        }
        return enviados;
    }
}
