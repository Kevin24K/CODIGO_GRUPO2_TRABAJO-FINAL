package pe.edu.upc.demosi.controllers;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.RolDTCinsert;
import pe.edu.upc.demosi.dtos.RolDTCList;
import pe.edu.upc.demosi.entities.Rol;
import pe.edu.upc.demosi.servicesinterfaces.IRolService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/Roles")
public class ControllerRol {
    private final IRolService rS;
    private final ModelMapper modelMapper;

    public ControllerRol(IRolService rS, ModelMapper modelMapper) {
        this.rS = rS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<RolDTCList>> listar() {

        List<RolDTCList> lista = rS.list()
                .stream()
                .map(rol -> modelMapper.map(rol, RolDTCList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<RolDTCinsert> registrar(
            @Valid @RequestBody RolDTCinsert dto) {

        Rol rol = modelMapper.map(dto, Rol.class);

        rS.insert(rol);

        RolDTCinsert responseDTO =
                modelMapper.map(rol, RolDTCinsert.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rol.getIdRol())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rS.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<RolDTCinsert> actualizar(@Valid @RequestBody RolDTCinsert dto) {
        // 1. Verificar que el rol exista
        Optional<Rol> existente = rS.listId(dto.getIdRol());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un rol con el id: " + dto.getIdRol()
            );
        }

        // 2. Obtener el rol existente
        Rol rol = existente.get();

        // 3. Actualizar sus campos
        rol.setNombreRol(dto.getNombreRol());
        rol.setDescripcionRol(dto.getDescripcionRol());

        // 4. Guardar
        rS.update(rol);

        // 5. Convertir a DTO
        RolDTCinsert responseDTO = modelMapper.map(rol, RolDTCinsert.class);

        return ResponseEntity.ok(responseDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RolDTCList> buscarPorId(@PathVariable Long id) {
        Optional<Rol> rol = rS.listId(id);

        if (rol.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un rol con el id: " + id
            );
        }

        RolDTCList responseDTO = modelMapper.map(rol.get(), RolDTCList.class);
        return ResponseEntity.ok(responseDTO);
    }
}
