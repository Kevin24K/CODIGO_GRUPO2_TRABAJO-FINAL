package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.dtos.ObjetoDTCinsert;
import pe.edu.upc.demosi.dtos.ObjetoDTCList;
import pe.edu.upc.demosi.dtos.ObjetoCategoriaDTCList;
import pe.edu.upc.demosi.entities.Categoria;
import pe.edu.upc.demosi.entities.Objeto;
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

    public ControllerObjeto(IObjetoService oS, ModelMapper modelMapper,ICategoriaService cS) {
        this.oS = oS;
        this.cS=cS;
        this.modelMapper = modelMapper;

    }

    @GetMapping
    public ResponseEntity<List<ObjetoDTCList>> listar() {

        List<ObjetoDTCList> lista = oS.list()
                .stream()
                .map(objeto -> modelMapper.map(objeto, ObjetoDTCList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // HU48: listar objetos activos
    @GetMapping("/activos")
    public ResponseEntity<List<ObjetoDTCList>> listarActivos() {
        List<ObjetoDTCList> lista = oS.listarActivos()
                .stream()
                .map(objeto -> modelMapper.map(objeto, ObjetoDTCList.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    // HU49: listar objetos con su categoría
    @GetMapping("/con-categoria")
    public ResponseEntity<List<ObjetoCategoriaDTCList>> listarConCategoria() {
        return ResponseEntity.ok(oS.listarConCategoria());
    }

    @PostMapping
    public ResponseEntity<ObjetoDTCinsert> registrar(
            @Valid @RequestBody ObjetoDTCinsert dto) {
        // 1. Validamos y obtenemos la Categoría desde la base de datos
        Categoria categoria = cS.listId(dto.getIdCategoria())
                .orElseThrow(() ->
                        new ResourceNotFoundException( // Cambia por ResourceNotFoundException si tienes tu clase personalizada
                                "No existe la categoría con el id: " + dto.getIdCategoria()
                        ));

        // 2. Mapeamos de DTO a Entidad y le asignamos la categoría encontrada
        Objeto objeto = modelMapper.map(dto, Objeto.class);
        objeto.setCategoria(categoria);

        // 3. Guardamos en la base de datos
        oS.insert(objeto);

        // 4. Mapeamos de vuelta al DTO para armar la respuesta
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Llama al método delete del servicio (oS)
        oS.delete(id);
        return ResponseEntity.noContent().build();
    }
}
