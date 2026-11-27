package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.CoincidenciaDTCList;
import pe.edu.upc.demosi.dtos.CoincidenciaDTCinsert;
import pe.edu.upc.demosi.entities.Coincidencia;
import pe.edu.upc.demosi.entities.EstadoCoincidencia;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICoincidenciaService;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoCoincidenciaService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Coincidencias")
public class ControllerCoincidencia {
    private final ICoincidenciaService cR;
    private final IEstadoCoincidenciaService eR;
    private final ModelMapper modelMapper;

    public ControllerCoincidencia(ICoincidenciaService cR, ModelMapper modelMapper,IEstadoCoincidenciaService eR) {
        this.cR = cR;
        this.eR=eR;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CoincidenciaDTCList>> listar() {

        List<CoincidenciaDTCList> lista = cR.list()
                .stream()
                .map(categoria -> modelMapper.map(categoria, CoincidenciaDTCList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<CoincidenciaDTCinsert> registrar(
            @Valid @RequestBody CoincidenciaDTCinsert dto) {
        // 1. Validamos y obtenemos el Rol
        EstadoCoincidencia estado = eR.listId(dto.getIdEstadoCoincidencia())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el estado coincidencia con id: " + dto.getIdEstadoCoincidencia()
                        ));
        // 3. SOLUCIÓN: Faltaba crear la variable 'act' y pasarle la contraseña y el rol
        Coincidencia Co = modelMapper.map(dto, Coincidencia.class);
        Co.setEstadoCoincidencia(estado);

        // 3. Guardamos
        cR.insert(Co);

        // 4. Mapeamos de vuelta al DTO para la respuesta
        CoincidenciaDTCinsert responseDTO = modelMapper.map(Co, CoincidenciaDTCinsert.class);

        // 5. Generamos la URL y devolvemos la respuesta
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(Co.getIdCoincidencia())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Llama al método delete del servicio (cS)
        cR.delete(id);
        return ResponseEntity.noContent().build();
    }
    // Buscar coincidencia por ID
    @GetMapping("/{id}")
    public ResponseEntity<CoincidenciaDTCList> buscarPorId(@PathVariable Long id) {
        Optional<Coincidencia> coincidencia = cR.listId(id);

        if (coincidencia.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe una coincidencia con el id: " + id
            );
        }

        CoincidenciaDTCList responseDTO = modelMapper.map(
                coincidencia.get(), CoincidenciaDTCList.class
        );
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar coincidencia
    @PutMapping
    public ResponseEntity<CoincidenciaDTCList> actualizar(@RequestBody CoincidenciaDTCList dto) {

        // 1. Verificar que la coincidencia exista
        Optional<Coincidencia> existente = cR.listId(dto.getIdCoincidencia());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe una coincidencia con el id: " + dto.getIdCoincidencia()
            );
        }

        // 2. Obtener la coincidencia existente y actualizar campos
        Coincidencia coincidencia = existente.get();
        coincidencia.setPorcentajeSimilitudC(dto.getPorcentajeSimilitudC());
        coincidencia.setDetalleC(dto.getDetalleC());
        coincidencia.setFechaGeneracionC(dto.getFechaGeneracionC());

        // 3. Guardar
        cR.update(coincidencia);

        // 4. Convertir a DTO
        CoincidenciaDTCList responseDTO = modelMapper.map(
                coincidencia, CoincidenciaDTCList.class
        );
        return ResponseEntity.ok(responseDTO);
    }
}
