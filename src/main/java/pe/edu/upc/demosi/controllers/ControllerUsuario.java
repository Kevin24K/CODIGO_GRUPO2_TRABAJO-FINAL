package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.UsuarioActivoDTCList;
import pe.edu.upc.demosi.dtos.UsuarioDTCList;
import pe.edu.upc.demosi.dtos.UsuarioDTCinsert;
import pe.edu.upc.demosi.entities.Rol;
import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.IRolService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Usuarios")
public class ControllerUsuario {

    private final IUsuarioService uS;
    private final IRolService rS;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public ControllerUsuario(IUsuarioService uS, IRolService rS, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.uS = uS;
        this.rS = rS;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
   }

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTCList>> listar() {
        List<UsuarioDTCList> lista = uS.list()
                .stream()
                .map(this::toListDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTCList> registrar(@Valid @RequestBody UsuarioDTCinsert dto) {

        Rol rol = rS.listId(dto.getIdRol())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe el rol con el id: " + dto.getIdRol()));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasenaHash(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(rol);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setActivo(true);

        uS.insert(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getIdUsuario())
                .toUri();

        return ResponseEntity.created(location).body(toListDTO(usuario));
    }

    // HU46: listar usuarios activos
    @GetMapping("/activos")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioActivoDTCList>> listarActivos() {
        List<UsuarioActivoDTCList> lista = uS.listarActivos()
                .stream()
                .map(u -> {
                    UsuarioActivoDTCList dto = new UsuarioActivoDTCList();
                    dto.setIdUsuario(u.getIdUsuario());
                    dto.setNombre(u.getNombre());
                    dto.setApellido(u.getApellido());
                    dto.setCorreo(u.getCorreo());
                    dto.setFechaRegistro(u.getFechaRegistro());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    // HU47: listar usuarios activos con su rol
    @GetMapping("/activos-con-rol")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTCList>> listarActivosConRol() {
        return ResponseEntity.ok(uS.listarActivosConRol());
    }

    private UsuarioDTCList toListDTO(Usuario u) {
        UsuarioDTCList dto = new UsuarioDTCList();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setCorreo(u.getCorreo());
        dto.setFechaRegistro(u.getFechaRegistro());
        dto.setActivo(u.isActivo());
        dto.setIdRol(u.getRol().getIdRol());
        dto.setNombreRol(u.getRol().getNombreRol());
        return dto;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Llama al método delete del servicio (uS)
        uS.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTCinsert> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = uS.listId(id);

        if (usuario.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un usuario con el id: " + id
            );
        }

        UsuarioDTCinsert responseDTO = modelMapper.map(usuario.get(), UsuarioDTCinsert.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Actualizar usuario
    @PutMapping
    public ResponseEntity<UsuarioDTCinsert> actualizar(@Valid @RequestBody UsuarioDTCinsert dto) {

        // 1. Verificar que el usuario exista
        Optional<Usuario> existente = uS.listId(dto.getIdUsuario());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un usuario con el id: " + dto.getNombre()
            );
        }

        // 2. Verificar que el rol exista
        Optional<Rol> rol = rS.listId(dto.getIdRol());

        if (rol.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un rol con el id: " + dto.getIdRol()
            );
        }

        // 3. Obtener el usuario existente
        Usuario usuario = existente.get();

        // 4. Actualizar sus campos
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());

        // 5. Asignar el rol existente
        usuario.setRol(rol.get());

        // 6. Guardar
        uS.update(usuario);

        // 7. Convertir a DTO
        UsuarioDTCinsert responseDTO = modelMapper.map(usuario, UsuarioDTCinsert.class);

        return ResponseEntity.ok(responseDTO);
    }
}