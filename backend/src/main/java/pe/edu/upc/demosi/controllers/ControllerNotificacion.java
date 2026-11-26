package pe.edu.upc.demosi.controllers;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.NotificacionDTCinsert;
import pe.edu.upc.demosi.dtos.NotificacionDTCList;
import pe.edu.upc.demosi.entities.Coincidencia;
import pe.edu.upc.demosi.entities.Notificacion;
import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICoincidenciaService;
import pe.edu.upc.demosi.servicesinterfaces.INotificacionService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
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

    public ControllerNotificacion(INotificacionService nS, IUsuarioService uS, ICoincidenciaService cS, ModelMapper modelMapper) {
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
}