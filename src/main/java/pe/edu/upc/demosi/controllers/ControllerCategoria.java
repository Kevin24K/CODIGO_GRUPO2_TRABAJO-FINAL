package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.CategoriaDTCList;
import pe.edu.upc.demosi.dtos.CategoriaDTCinsert;
import pe.edu.upc.demosi.dtos.CategoriaDTODetail;
import pe.edu.upc.demosi.dtos.CategoriaDTOUpdate;
import pe.edu.upc.demosi.entities.Categoria;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICategoriaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/Categorias")
public class ControllerCategoria {

    private final ICategoriaService cS;
    private final ModelMapper modelMapper;

    public ControllerCategoria(ICategoriaService cS, ModelMapper modelMapper) {
        this.cS = cS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoriaDTCList>> listar() {
        List<CategoriaDTCList> lista = cS.list()
                .stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTCList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CategoriaDTCinsert> registrar(
            @Valid @RequestBody CategoriaDTCinsert dto) {
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        cS.insert(categoria);
        CategoriaDTCinsert responseDTO = modelMapper.map(categoria, CategoriaDTCinsert.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.getIdCategoria())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Categoria categoria = cS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una categoría con el id: " + id
                        )
                );
        cS.delete(categoria.getIdCategoria());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CategoriaDTODetail> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTOUpdate dto) {
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        cS.update(id, categoria);
        CategoriaDTODetail responseDTO = modelMapper.map(cS.listId(id), CategoriaDTODetail.class);
        return ResponseEntity.ok(responseDTO);
    }
}