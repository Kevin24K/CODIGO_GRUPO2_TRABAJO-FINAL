package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.EstadoReporteDTOList;
import pe.edu.upc.demosi.dtos.EstadoReporteDTOinsert;
import pe.edu.upc.demosi.entities.EstadoReporte;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoReporteService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/EstadoReporte")
public class ControllerEstadoReporte {
    private final IEstadoReporteService eR;
    private final ModelMapper modelMapper;

    public ControllerEstadoReporte(IEstadoReporteService eR, ModelMapper modelMapper) {
        this.eR = eR;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    public ResponseEntity<List<EstadoReporteDTOList>>listar() {

        List<EstadoReporteDTOList> lista = eR.list()
                .stream()
                .map(categoria -> modelMapper.map(categoria, EstadoReporteDTOList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<EstadoReporteDTOinsert> registrar(
            @Valid @RequestBody EstadoReporteDTOinsert dto) {
        EstadoReporte eRport = modelMapper.map(dto, EstadoReporte.class);

        eR.insert(eRport);

        EstadoReporteDTOinsert responseDTO =
                modelMapper.map(eRport, EstadoReporteDTOinsert.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eRport.getIdEstadoReporte())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<EstadoReporteDTOinsert> buscarPorId(@PathVariable Long id) {
        EstadoReporte estado = eR.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estado de reporte con el id: " + id
                ));

        EstadoReporteDTOinsert responseDTO = modelMapper.map(estado, EstadoReporteDTOinsert.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar estado de reporte
    @PutMapping
    public ResponseEntity<EstadoReporteDTOinsert> actualizar(@Valid @RequestBody EstadoReporteDTOinsert dto) {
        // 1. Verificar que el estado de reporte exista
        Optional<EstadoReporte> existente = eR.listId(dto.getIdEstadoReporte());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un estado de reporte con el id: " + dto.getIdEstadoReporte()
            );
        }

        // 2. Obtener el registro existente
        EstadoReporte estadoReporte = existente.get();

        // 3. Actualizar sus campos
        estadoReporte.setNombreEReporte(dto.getNombreEReporte());
        estadoReporte.setDescripcionEReporte(dto.getDescripcionEReporte());

        // 4. Guardar
        eR.update(estadoReporte);

        // 5. Convertir a DTO
        EstadoReporteDTOinsert responseDTO = modelMapper.map(estadoReporte, EstadoReporteDTOinsert.class);

        return ResponseEntity.ok(responseDTO);
    }

    // Eliminar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        EstadoReporte estado = eR.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estado de reporte con el id: " + id
                ));
        eR.delete(estado.getIdEstadoReporte());
        return ResponseEntity.noContent().build();
    }
}
