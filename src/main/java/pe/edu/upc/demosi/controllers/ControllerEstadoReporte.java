package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.*;
import pe.edu.upc.demosi.entities.EstadoReporte;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoReporteService;

import java.net.URI;
import java.util.List;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EstadoReporteDTOList>> listar() {
        List<EstadoReporteDTOList> lista = eR.list()
                .stream()
                .map(categoria -> modelMapper.map(categoria, EstadoReporteDTOList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<EstadoReporteDTOinsert> registrar(
            @Valid @RequestBody EstadoReporteDTOinsert dto) {
        EstadoReporte eRport = modelMapper.map(dto, EstadoReporte.class);
        eR.insert(eRport);
        EstadoReporteDTOinsert responseDTO = modelMapper.map(eRport, EstadoReporteDTOinsert.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eRport.getIdEstadoReporte())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        EstadoReporte estadoreporte = eR.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un estado de reporte con el id: " + id
                        )
                );
        eR.delete(estadoreporte.getIdEstadoReporte());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<EstadoReporteDTODetail> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstadoReporteDTOUpdate dto) {
        EstadoReporte estadoreporte = modelMapper.map(dto, EstadoReporte.class);
        eR.update(id, estadoreporte);
        EstadoReporteDTODetail responseDTO = modelMapper.map(eR.listId(id), EstadoReporteDTODetail.class);
        return ResponseEntity.ok(responseDTO);
    }
}