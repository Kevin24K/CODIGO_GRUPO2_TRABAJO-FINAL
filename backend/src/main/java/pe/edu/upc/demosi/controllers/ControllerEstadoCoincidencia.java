package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.EstadoCoincidenciaDTOinsert;
import pe.edu.upc.demosi.entities.EstadoCoincidencia;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoCoincidenciaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/EstadoCoincidencia")
public class ControllerEstadoCoincidencia {
    private final IEstadoCoincidenciaService eR;
    private final ModelMapper modelMapper;

    public ControllerEstadoCoincidencia(IEstadoCoincidenciaService eR, ModelMapper modelMapper) {
        this.eR = eR;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<EstadoCoincidenciaDTOinsert>> listar() {

        List<EstadoCoincidenciaDTOinsert> lista = eR.list()
                .stream()
                .map(rol -> modelMapper.map(rol, EstadoCoincidenciaDTOinsert.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<EstadoCoincidenciaDTOinsert> registrar(
            @Valid @RequestBody EstadoCoincidenciaDTOinsert dto) {

        EstadoCoincidencia eC = modelMapper.map(dto,EstadoCoincidencia .class);

        eR.insert(eC);

        EstadoCoincidenciaDTOinsert responseDTO =
                modelMapper.map(eC, EstadoCoincidenciaDTOinsert.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eC.getIdEstadoCoincidencia())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    @GetMapping("/pendientes")
    public ResponseEntity<List<EstadoCoincidencia>> listarPendientes() {
        List<EstadoCoincidencia> lista = eR.listPendientes(); // eR es tu IEstadoCoincidenciaRepository
        return ResponseEntity.ok(lista); // CA02: HTTP 200 OK
    }
    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<EstadoCoincidenciaDTOinsert> buscarPorId(@PathVariable Long id) {
        Optional<EstadoCoincidencia> existente = eR.listId(id);

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un estado de coincidencia con el id: " + id
            );
        }

        EstadoCoincidenciaDTOinsert responseDTO = modelMapper.map(
                existente.get(), EstadoCoincidenciaDTOinsert.class
        );
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar estado de coincidencia
    @PutMapping
    public ResponseEntity<EstadoCoincidenciaDTOinsert> actualizar(
            @Valid @RequestBody EstadoCoincidenciaDTOinsert dto) {

        // 1. Verificar que el estado de coincidencia exista
        Optional<EstadoCoincidencia> existente = eR.listId(dto.getIdEstadoCoincidencia());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un estado de coincidencia con el id: " + dto.getIdEstadoCoincidencia()
            );
        }

        // 2. Obtener el registro existente
        EstadoCoincidencia estadoCoincidencia = existente.get();

        // 3. Actualizar sus campos
        estadoCoincidencia.setNombreEC(dto.getNombreEC());
        estadoCoincidencia.setDescripcionEC(dto.getDescripcionEC());

        // 4. Guardar
        eR.update(estadoCoincidencia);

        // 5. Convertir a DTO
        EstadoCoincidenciaDTOinsert responseDTO = modelMapper.map(
                estadoCoincidencia, EstadoCoincidenciaDTOinsert.class
        );

        return ResponseEntity.ok(responseDTO);
    }

    // Eliminar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        EstadoCoincidencia estadoCoincidencia = eR.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estado de coincidencia con el id: " + id
                ));

        eR.delete(estadoCoincidencia.getIdEstadoCoincidencia());
        return ResponseEntity.noContent().build();
    }
}
