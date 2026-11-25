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
}
