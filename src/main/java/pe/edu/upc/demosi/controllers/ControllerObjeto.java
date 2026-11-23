package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.*;
import pe.edu.upc.demosi.entities.Categoria;
import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.ICategoriaService;
import pe.edu.upc.demosi.servicesinterfaces.IObjetoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/Objetos")
public class ControllerObjeto {

    private final IObjetoService oS;
    private final ICategoriaService cS;
    private final ModelMapper modelMapper;

    public ControllerObjeto(IObjetoService oS, ModelMapper modelMapper, ICategoriaService cS) {
        this.oS = oS;
        this.cS = cS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ObjetoDTCList>> listar() {
        List<ObjetoDTCList> lista = oS.list()
                .stream()
                .map(objeto -> modelMapper.map(objeto, ObjetoDTCList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/activos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ObjetoDTCList>> listarActivos() {
        List<ObjetoDTCList> lista = oS.listarActivos()
                .stream()
                .map(objeto -> modelMapper.map(objeto, ObjetoDTCList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/con-categoria")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ObjetoCategoriaDTCList>> listarConCategoria() {
        return ResponseEntity.ok(oS.listarConCategoria());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ObjetoDTCinsert> registrar(
            @Valid @RequestBody ObjetoDTCinsert dto) {
        Categoria categoria = cS.listId(dto.getIdCategoria())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe la categoría con el id: " + dto.getIdCategoria()
                        ));
        Objeto objeto = modelMapper.map(dto, Objeto.class);
        objeto.setCategoria(categoria);
        oS.insert(objeto);
        ObjetoDTCinsert responseDTO = modelMapper.map(objeto, ObjetoDTCinsert.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(objeto.getIdObjeto())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Objeto objeto = oS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un objeto con el id: " + id
                        )
                );
        oS.delete(objeto.getIdObjeto());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjetoDTODetail> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ObjetoDTOUpdate dto) {
        Objeto objeto = modelMapper.map(dto, Objeto.class);
        oS.update(id, objeto);
        ObjetoDTODetail responseDTO = modelMapper.map(oS.listId(id), ObjetoDTODetail.class);
        return ResponseEntity.ok(responseDTO);
    }
}