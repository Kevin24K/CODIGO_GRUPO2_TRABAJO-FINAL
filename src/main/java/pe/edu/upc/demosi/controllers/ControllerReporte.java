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
import pe.edu.upc.demosi.servicesinterfaces.IEstadoReporteService;
import pe.edu.upc.demosi.servicesinterfaces.IObjetoService;
import pe.edu.upc.demosi.servicesinterfaces.IReporteService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/Reportes")
public class ControllerReporte {

    private final IReporteService rS;
    private final IUsuarioService uS;
    private final IEstadoReporteService eR;
    private final IObjetoService oS;
    private final ModelMapper modelMapper;

    public ControllerReporte(IReporteService rS, ModelMapper modelMapper, IEstadoReporteService eR, IObjetoService oS, IUsuarioService uS) {
        this.rS = rS;
        this.uS = uS;
        this.eR = eR;
        this.oS = oS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReporteDTCList>> listar() {
        List<ReporteDTCList> lista = rS.list()
                .stream()
                .map(reporte -> modelMapper.map(reporte, ReporteDTCList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReporteDTCinsert> registrar(
            @Valid @RequestBody ReporteDTCinsert dto) {
        Usuario usuario = uS.listId(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id: " + dto.getIdUsuario()));
        Objeto objeto = oS.listId(dto.getIdObjeto())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el objeto con id: " + dto.getIdObjeto()));
        EstadoReporte estado = eR.listId(dto.getIdEstadoReporte())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estado de reporte con id: " + dto.getIdEstadoReporte()));
        Reporte repS = modelMapper.map(dto, Reporte.class);
        repS.setUsuario(usuario);
        repS.setObjeto(objeto);
        repS.setEstadoReporte(estado);
        repS.setFechaCreacion(LocalDateTime.now());
        repS.setFechaActualizacionR(LocalDateTime.now());
        rS.insert(repS);
        ReporteDTCinsert responseDTO = modelMapper.map(repS, ReporteDTCinsert.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(repS.getIdReporte())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/activos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Reporte>> listarActivos() {
        List<Reporte> lista = rS.listActivos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Reporte reporte = rS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un reporte con el id: " + id
                        )
                );
        rS.delete(reporte.getIdReporte());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReporteDTODetail> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReporteDTOUpdate dto) {
        Reporte reporte = modelMapper.map(dto, Reporte.class);
        rS.update(id, reporte);
        ReporteDTODetail responseDTO = modelMapper.map(rS.listId(id), ReporteDTODetail.class);
        return ResponseEntity.ok(responseDTO);
    }
}