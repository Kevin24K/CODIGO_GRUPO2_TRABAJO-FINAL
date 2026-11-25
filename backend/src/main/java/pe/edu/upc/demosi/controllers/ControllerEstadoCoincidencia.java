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
}
