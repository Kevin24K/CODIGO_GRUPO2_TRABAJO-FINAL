package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.modelmapper.ModelMapper;
import pe.edu.upc.demosi.dtos.*;
import pe.edu.upc.demosi.entities.*;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.servicesinterfaces.IRolService;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/Usuarios")
public class ControllerUsuario {

    private final IUsuarioService uS;
    private final IRolService rS;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public ControllerUsuario(IUsuarioService uS, IRolService rS, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.uS = uS;
        this.rS = rS;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTCList>> listar() {
        List<UsuarioDTCList> lista = uS.list()
                .stream()
                .map(this::toListDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/activos")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/activos-con-rol")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Usuario usuario = uS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: " + id
                        )
                );
        uS.delete(usuario.getIdUsuario());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTODetail> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTOUpdate dto) {
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        uS.update(id, usuario);
        UsuarioDTODetail responseDTO = modelMapper.map(uS.listId(id), UsuarioDTODetail.class);
        return ResponseEntity.ok(responseDTO);
    }
}