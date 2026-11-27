package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.ReporteDTCList;
import pe.edu.upc.demosi.dtos.ReporteDTCinsert;
import pe.edu.upc.demosi.dtos.TotalDTO;
import pe.edu.upc.demosi.entities.EstadoReporte;
import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.entities.Reporte;
import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICoincidenciaAutomaticaService;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoReporteService;
import pe.edu.upc.demosi.servicesinterfaces.IObjetoService;
import pe.edu.upc.demosi.servicesinterfaces.IReporteService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Reportes")
public class ControllerReporte {
    private final IReporteService rS;
    private final IUsuarioService uS;
    private final IEstadoReporteService eR;
    private final IObjetoService oS;
    private final ModelMapper modelMapper;
    private final ICoincidenciaAutomaticaService caS; // Notificaciones automáticas

    public ControllerReporte(IReporteService rS, ModelMapper modelMapper, IEstadoReporteService eR, IObjetoService oS, IUsuarioService uS,
                             ICoincidenciaAutomaticaService caS) {
        this.rS = rS;
        this.caS = caS;
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

        // Notificaciones: compara con los reportes del tipo contrario y avisa si hay >= 80 %
        try {
            caS.procesarReporte(repS);
        } catch (Exception e) {
            // Si falla la comparación, el reporte igual queda registrado
        }

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
    @GetMapping("/total-category")
    public List<TotalDTO> getTotalByCategory() {
        return rS.getTotalByCategory();
    }
    @PutMapping
    public ResponseEntity<ReporteDTCinsert> actualizar(@Valid @RequestBody ReporteDTCinsert dto) {

        // 1. Verificar que el reporte exista
        Optional<Reporte> existente = rS.listId(dto.getIdReporte());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un reporte con el id: " + dto.getIdReporte()
            );
        }

        // 2. Verificar que el usuario exista
        Optional<Usuario> usuario = uS.listId(dto.getIdUsuario());

        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un usuario con el id: " + dto.getIdUsuario()
            );
        }

        // 3. Verificar que el objeto exista
        Optional<Objeto> objeto = oS.listId(dto.getIdObjeto());

        if (objeto.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un objeto con el id: " + dto.getIdObjeto()
            );
        }

        // 4. Verificar que el estado de reporte exista
        Optional<EstadoReporte> estadoReporte = eR.listId(dto.getIdEstadoReporte());

        if (estadoReporte.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un estado de reporte con el id: " + dto.getIdEstadoReporte()
            );
        }

        // 5. Obtener el reporte existente y actualizar campos
        Reporte reporte = existente.get();
        reporte.setTipoReporte(dto.getTipoReporte());
        reporte.setFechaeventoR(dto.getFechaeventoR());
        reporte.setHoraEventoR(dto.getHoraEventoR());

        // 6. Asignar las entidades relacionadas existentes
        reporte.setUsuario(usuario.get());
        reporte.setObjeto(objeto.get());
        reporte.setEstadoReporte(estadoReporte.get());

        // 7. Guardar
        rS.update(reporte);

        // 8. Convertir a DTO y asegurar IDs de relaciones en la respuesta
        ReporteDTCinsert responseDTO = modelMapper.map(reporte, ReporteDTCinsert.class);
        responseDTO.setIdUsuario(usuario.get().getIdUsuario());
        responseDTO.setIdObjeto(objeto.get().getIdObjeto());
        responseDTO.setIdEstadoReporte(estadoReporte.get().getIdEstadoReporte());

        return ResponseEntity.ok(responseDTO);
    }
    // Buscar reporte por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTCinsert> buscarPorId(@PathVariable Long id) {
        Optional<Reporte> reporte = rS.listId(id);

        if (reporte.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un reporte con el id: " + id
            );
        }

        Reporte reporteEntity = reporte.get();
        ReporteDTCinsert responseDTO = modelMapper.map(reporteEntity, ReporteDTCinsert.class);

        // Asegurar los IDs de las relaciones (@ManyToOne) si ModelMapper no los asigna automáticamente
        if (reporteEntity.getUsuario() != null) {
            responseDTO.setIdUsuario(reporteEntity.getUsuario().getIdUsuario());
        }
        if (reporteEntity.getObjeto() != null) {
            responseDTO.setIdObjeto(reporteEntity.getObjeto().getIdObjeto());
        }
        if (reporteEntity.getEstadoReporte() != null) {
            responseDTO.setIdEstadoReporte(reporteEntity.getEstadoReporte().getIdEstadoReporte());
        }

        return ResponseEntity.ok(responseDTO);
    }
}