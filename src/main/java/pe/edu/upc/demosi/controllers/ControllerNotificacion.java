package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.*;
import pe.edu.upc.demosi.entities.*;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICoincidenciaService;
import pe.edu.upc.demosi.servicesinterfaces.INotificacionService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificacionDTCList>> listar() {
        List<NotificacionDTCList> lista = nS.list()
                .stream()
                .map(notificacion -> modelMapper.map(notificacion, NotificacionDTCList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Notificacion notificacion = nS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una notificación con el id: " + id
                        )
                );
        nS.delete(notificacion.getIdNotificacion());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<NotificacionDTODetail> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody NotificacionDTOUpdate dto) {
        Notificacion notificacion = modelMapper.map(dto, Notificacion.class);
        nS.update(id, notificacion);
        NotificacionDTODetail responseDTO = modelMapper.map(nS.listId(id), NotificacionDTODetail.class);
        return ResponseEntity.ok(responseDTO);
    }
}