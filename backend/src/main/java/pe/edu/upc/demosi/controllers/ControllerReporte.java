package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.ReporteDTCList;
import pe.edu.upc.demosi.dtos.ReporteDTCinsert;
import pe.edu.upc.demosi.entities.EstadoReporte;
import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.entities.Reporte;
import pe.edu.upc.demosi.entities.Usuario;
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

    // 1. MÉTODO PARA LISTAR (Usa el DTCList)
    @GetMapping
    public ResponseEntity<List<ReporteDTCList>> listar() {

        List<ReporteDTCList> lista = rS.list()
                .stream()
                .map(reporte -> modelMapper.map(reporte, ReporteDTCList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // 2. MÉTODO PARA CREAR (Usa el DTCinsert)
    @PostMapping
    public ResponseEntity<ReporteDTCinsert> registrar(
            @Valid @RequestBody ReporteDTCinsert dto) {
        // 1. Validamos las 3 dependencias (Lanzando 404 si alguna falla)
        Usuario usuario = uS.listId(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id: " + dto.getIdUsuario()));

        Objeto objeto = oS.listId(dto.getIdObjeto())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el objeto con id: " + dto.getIdObjeto()));

        EstadoReporte estado = eR.listId(dto.getIdEstadoReporte())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el estado de reporte con id: " + dto.getIdEstadoReporte()));

        // 2. Mapeamos y asignamos las 3 entidades validadas
        Reporte repS = modelMapper.map(dto, Reporte.class);
        repS.setUsuario(usuario);
        repS.setObjeto(objeto);
        repS.setEstadoReporte(estado);

        // --- ALTERNATIVA A @PREPERSIST: Asignación manual de fechas ---
        repS.setFechaCreacion(LocalDateTime.now());
        repS.setFechaActualizacionR(LocalDateTime.now());

        // 3. Guardamos
        rS.insert(repS);

        // 4. Mapeamos a la respuesta y le inyectamos los IDs
        ReporteDTCinsert responseDTO = modelMapper.map(repS, ReporteDTCinsert.class);

        // 5. Devolvemos el 201 Created
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
    public ResponseEntity<List<Reporte>> listarActivos() {
        List<Reporte> lista = rS.listActivos(); // rS es tu IReporteService
        return ResponseEntity.ok(lista); // CA02: Responder con HTTP 200 OK
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rS.delete(id);
        return ResponseEntity.noContent().build();
    }
}