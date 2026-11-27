package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.dtos.ObjetoDTCinsert;
import pe.edu.upc.demosi.dtos.ObjetoDTCList;
import pe.edu.upc.demosi.dtos.ObjetoFotoDTO;
import pe.edu.upc.demosi.dtos.ObjetoSimilarDTO;
import pe.edu.upc.demosi.dtos.BusquedaTextoDTO;
import pe.edu.upc.demosi.dtos.ObjetoCategoriaDTCList;
import pe.edu.upc.demosi.entities.Categoria;
import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.servicesinterfaces.ICategoriaService;
import pe.edu.upc.demosi.servicesinterfaces.IIaService;
import pe.edu.upc.demosi.servicesinterfaces.IObjetoService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Objetos")
public class ControllerObjeto {
    private final IObjetoService oS;
    private final ICategoriaService cS;
    private final ModelMapper modelMapper;
    private final IIaService iaS; // HUF17 / HUF19

    public ControllerObjeto(IObjetoService oS, ModelMapper modelMapper,ICategoriaService cS, IIaService iaS) {
        this.oS = oS;
        this.cS=cS;
        this.modelMapper = modelMapper;
        this.iaS = iaS;

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

        // HUF19: si viene foto, la IA genera su descripción
        objeto.setDescripcionIaObjeto(iaS.describirImagen(dto.getFotoObjeto()));

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
    // HUF19: agregar o cambiar la foto (comprimida en el front) y su descripción IA
    @PutMapping("/{id}/foto")
    public ResponseEntity<ObjetoDTCList> actualizarFoto(
            @PathVariable Long id,
            @Valid @RequestBody ObjetoFotoDTO dto) {
        Objeto objeto = oS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe el objeto con el id: " + id));
        objeto.setFotoObjeto(dto.getFotoObjeto());
        objeto.setDescripcionIaObjeto(iaS.describirImagen(dto.getFotoObjeto()));
        oS.insert(objeto); // save() actualiza porque ya tiene id
        return ResponseEntity.ok(modelMapper.map(objeto, ObjetoDTCList.class));
    }

    // HUF17: buscar objetos parecidos a lo que escribe quien perdió algo
    @PostMapping("/buscar-similares")
    public ResponseEntity<List<ObjetoSimilarDTO>> buscarSimilares(
            @Valid @RequestBody BusquedaTextoDTO dto) {
        int top = (dto.getTop() == null || dto.getTop() < 1) ? 8 : dto.getTop();
        return ResponseEntity.ok(iaS.buscarSimilares(dto.getTexto(), top));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Llama al método delete del servicio (oS)
        oS.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ObjetoDTCinsert> buscarPorId(@PathVariable Long id) {
        Optional<Objeto> objeto = oS.listId(id);

        if (objeto.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un objeto con el id: " + id
            );
        }

        ObjetoDTCinsert responseDTO = modelMapper.map(objeto.get(), ObjetoDTCinsert.class);
        return ResponseEntity.ok(responseDTO);
    }
    @PutMapping
    public ResponseEntity<ObjetoDTCinsert> actualizar(@Valid @RequestBody ObjetoDTCinsert dto) {

        // 1. Verificar que el objeto exista
        Optional<Objeto> existente = oS.listId(dto.getIdObjeto());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un objeto con el id: " + dto.getIdObjeto()
            );
        }

        // 2. Verificar que la categoría exista
        Optional<Categoria> categoria = cS.listId(dto.getIdCategoria());

        if (categoria.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe una categoría con el id: " + dto.getIdCategoria()
            );
        }

        // 3. Obtener el objeto existente y actualizar campos
        Objeto objeto = existente.get();
        objeto.setNombreObjeto(dto.getNombreObjeto());
        objeto.setDescripcionObjeto(dto.getDescripcionObjeto());
        objeto.setColorObjeto(dto.getColorObjeto());
        objeto.setMarcaObjeto(dto.getMarcaObjeto());
        objeto.setActivoObjeto(dto.getActivoObjeto());

        // 4. Asignar la categoría existente
        objeto.setCategoria(categoria.get());

        // 5. Guardar
        oS.update(objeto);

        // 6. Convertir a DTO y asegurar idCategoria en la respuesta
        ObjetoDTCinsert responseDTO = modelMapper.map(objeto, ObjetoDTCinsert.class);
        responseDTO.setIdCategoria(categoria.get().getIdCategoria());

        return ResponseEntity.ok(responseDTO);
    }
}
