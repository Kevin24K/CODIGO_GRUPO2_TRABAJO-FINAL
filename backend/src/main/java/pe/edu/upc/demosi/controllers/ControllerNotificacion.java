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
}