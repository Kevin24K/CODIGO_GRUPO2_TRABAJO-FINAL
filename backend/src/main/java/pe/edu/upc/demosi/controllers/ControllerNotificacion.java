package pe.edu.upc.demosi.controllers;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.NotificacionDTCinsert;
import pe.edu.upc.demosi.dtos.NotificacionDTCList;
import pe.edu.upc.demosi.dtos.NotificacionDetalleDTO;
import pe.edu.upc.demosi.dtos.PushTokenDTO;
import pe.edu.upc.demosi.servicesinterfaces.IPushService;
import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.entities.Reporte;
import pe.edu.upc.demosi.entities.Coincidencia;
import pe.edu.upc.demosi.entities.Notificacion;
import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICoincidenciaService;
import pe.edu.upc.demosi.servicesinterfaces.INotificacionService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.security.Principal;
import java.util.Comparator;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Notificaciones")
public class ControllerNotificacion {
    private final INotificacionService nS;
    private final IUsuarioService uS;
    private final ICoincidenciaService cS;
    private final ModelMapper modelMapper;
    private final IPushService pS; // Firebase Cloud Messaging

    public ControllerNotificacion(INotificacionService nS, IUsuarioService uS, ICoincidenciaService cS, ModelMapper modelMapper,
                                 IPushService pS) {
        this.pS = pS;
        this.nS = nS;
        this.uS = uS;
        this.cS = cS;
        this.modelMapper = modelMapper;
    }

    // 1. MÉTODO PARA LISTAR
    @GetMapping
    public ResponseEntity<List<NotificacionDTCList>> listar() {
        List<NotificacionDTCList> lista = nS.list()
                .stream()
                .map(notificacion -> modelMapper.map(notificacion, NotificacionDTCList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // 2. MÉTODO PARA CREAR
    @PostMapping
    public ResponseEntity<NotificacionDTCinsert> registrar(
            @Valid @RequestBody NotificacionDTCinsert dto) {

        Usuario usuario = uS.listId(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con el id: " + dto.getIdUsuario()
                ));

        Coincidencia coincidencia = cS.listId(dto.getIdCoincidencia())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la coincidencia con el id: " + dto.getIdCoincidencia()
                ));

        Notificacion noti = modelMapper.map(dto, Notificacion.class);
        noti.setUsuario(usuario);
        noti.setCoincidencia(coincidencia);

        noti.setFechaCreacionN(LocalDateTime.now());
        nS.insert(noti);

        NotificacionDTCinsert responseDTO = modelMapper.map(noti, NotificacionDTCinsert.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(noti.getIdNotificacion())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Llama al método delete del servicio (nS)
        nS.delete(id);
        return ResponseEntity.noContent().build();
    }
    // Actualizar notificación
    @PutMapping
    public ResponseEntity<NotificacionDTCList> actualizar(@RequestBody NotificacionDTCList dto) {

        // 1. Verificar que la notificación exista
        Optional<Notificacion> existente = nS.listId(dto.getIdNotificacion());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe una notificación con el id: " + dto.getIdNotificacion()
            );
        }

        // 2. Verificar que el usuario exista
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());

        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un usuario con el id: " + dto.getIdUsuario()
            );
        }

        // 3. Verificar que la coincidencia exista
        Optional<Coincidencia> coincidencia = cS.listId(dto.getIdCoincidencia());

        if (coincidencia.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe una coincidencia con el id: " + dto.getIdCoincidencia()
            );
        }

        // 4. Obtener la notificación existente y actualizar campos
        Notificacion notificacion = existente.get();
        notificacion.setTituloNotificacion(dto.getTituloNotificacion());
        notificacion.setMensajeNotificacion(dto.getMensajeNotificacion());
        notificacion.setTipoNotificacion(dto.getTipoNotificacion());
        notificacion.setLeidaNotificacion(dto.getLeidaNotificacion());
        notificacion.setFechaCreacionN(dto.getFechaCreacionN());

        // 5. Asignar las entidades relacionadas existentes
        notificacion.setUsuario(usuario.get());
        notificacion.setCoincidencia(coincidencia.get());

        // 6. Guardar
        nS.update(notificacion);

        // 7. Convertir a DTO y asegurar IDs de relaciones en la respuesta
        NotificacionDTCList responseDTO = modelMapper.map(notificacion, NotificacionDTCList.class);
        responseDTO.setIdUsuario(usuario.get().getIdUsuario());
        responseDTO.setIdCoincidencia(coincidencia.get().getIdCoincidencia());

        return ResponseEntity.ok(responseDTO);
    }

    // ================= Notificaciones del usuario conectado =================
    // El correo sale del token JWT; si no hay token se puede mandar ?correo=

    @GetMapping("/mias")
    public ResponseEntity<List<NotificacionDetalleDTO>> misNotificaciones(
            Principal principal, @RequestParam(required = false) String correo) {
        Usuario usuario = usuarioActual(principal, correo);
        List<NotificacionDetalleDTO> lista = nS.listarPorUsuario(usuario.getIdUsuario())
                .stream()
                .sorted(Comparator.comparing(Notificacion::getFechaCreacionN,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(this::aDetalle)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/mias/no-leidas")
    public ResponseEntity<Map<String, Long>> cantidadNoLeidas(
            Principal principal, @RequestParam(required = false) String correo) {
        Usuario usuario = usuarioActual(principal, correo);
        long cantidad = nS.listarPorUsuario(usuario.getIdUsuario()).stream()
                .filter(n -> !Boolean.TRUE.equals(n.getLeidaNotificacion()))
                .count();
        return ResponseEntity.ok(Map.of("cantidad", cantidad));
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<NotificacionDetalleDTO> marcarLeida(@PathVariable Long id) {
        Notificacion n = nS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una notificación con el id: " + id));
        n.setLeidaNotificacion(true);
        nS.update(n);
        return ResponseEntity.ok(aDetalle(n));
    }

    @PutMapping("/mias/leidas")
    public ResponseEntity<Map<String, Long>> marcarTodasLeidas(
            Principal principal, @RequestParam(required = false) String correo) {
        Usuario usuario = usuarioActual(principal, correo);
        long cambiadas = 0;
        for (Notificacion n : nS.listarPorUsuario(usuario.getIdUsuario())) {
            if (!Boolean.TRUE.equals(n.getLeidaNotificacion())) {
                n.setLeidaNotificacion(true);
                nS.update(n);
                cambiadas++;
            }
        }
        return ResponseEntity.ok(Map.of("cantidad", cambiadas));
    }

    // Firebase: el navegador manda su token FCM al activar las notificaciones push
    @PostMapping("/push-token")
    public ResponseEntity<Map<String, Object>> registrarTokenPush(
            Principal principal, @RequestParam(required = false) String correo,
            @Valid @RequestBody PushTokenDTO dto) {
        Usuario usuario = usuarioActual(principal, correo);
        pS.registrarToken(usuario, dto.getToken());
        return ResponseEntity.ok(Map.of("registrado", true, "firebaseActivo", pS.activo()));
    }

    @DeleteMapping("/push-token")
    public ResponseEntity<Void> eliminarTokenPush(@RequestParam String token) {
        pS.eliminarToken(token);
        return ResponseEntity.noContent().build();
    }

    private Usuario usuarioActual(Principal principal, String correo) {
        String email = (principal != null && principal.getName() != null) ? principal.getName() : correo;
        if (email == null || email.isBlank()) {
            throw new ResourceNotFoundException("No se pudo identificar al usuario (inicia sesión)");
        }
        return uS.list().stream()
                .filter(u -> email.equalsIgnoreCase(u.getCorreo()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con el correo: " + email));
    }

    private NotificacionDetalleDTO aDetalle(Notificacion n) {
        NotificacionDetalleDTO d = new NotificacionDetalleDTO();
        d.setIdNotificacion(n.getIdNotificacion());
        d.setTituloNotificacion(n.getTituloNotificacion());
        d.setMensajeNotificacion(n.getMensajeNotificacion());
        d.setTipoNotificacion(n.getTipoNotificacion());
        d.setLeidaNotificacion(n.getLeidaNotificacion());
        d.setFechaCreacionN(n.getFechaCreacionN());
        Coincidencia c = n.getCoincidencia();
        if (c != null) {
            d.setIdCoincidencia(c.getIdCoincidencia());
            d.setPorcentaje(c.getPorcentajeSimilitudC());
            if (c.getEstadoCoincidencia() != null) d.setEstadoCoincidencia(c.getEstadoCoincidencia().getNombreEC());
            Reporte perdido = c.getReportePerdido();
            if (perdido != null) {
                d.setIdReportePerdido(perdido.getIdReporte());
                if (perdido.getObjeto() != null) d.setObjetoPerdido(perdido.getObjeto().getNombreObjeto());
            }
            Reporte encontrado = c.getReporteEncontrado();
            if (encontrado != null) {
                d.setIdReporteEncontrado(encontrado.getIdReporte());
                d.setFechaEncontrado(encontrado.getFechaeventoR());
                Objeto o = encontrado.getObjeto();
                if (o != null) {
                    d.setIdObjetoEncontrado(o.getIdObjeto());
                    d.setObjetoEncontrado(o.getNombreObjeto());
                    d.setDescripcionEncontrado(o.getDescripcionObjeto());
                    d.setDescripcionIaEncontrado(o.getDescripcionIaObjeto());
                    d.setColorEncontrado(o.getColorObjeto());
                    d.setMarcaEncontrado(o.getMarcaObjeto());
                    d.setFotoEncontrado(o.getFotoObjeto());
                    if (o.getCategoria() != null) d.setCategoriaEncontrado(o.getCategoria().getNombreCategoria());
                }
            }
        }
        return d;
    }
}