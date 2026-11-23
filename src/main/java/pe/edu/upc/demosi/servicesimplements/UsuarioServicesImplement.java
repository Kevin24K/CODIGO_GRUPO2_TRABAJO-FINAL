package pe.edu.upc.demosi.servicesimplements;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.repositories.IUsuarioRepository;
import pe.edu.upc.demosi.servicesinterfaces.IUsuarioService;
import pe.edu.upc.demosi.dtos.UsuarioDTCList;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicesImplement implements IUsuarioService, UserDetailsService {

    private final IUsuarioRepository uR;

    public UsuarioServicesImplement(IUsuarioRepository uR) {
        this.uR = uR;
    }

    @Override
    public List<Usuario> list() {
        return uR.findAll();
    }

    @Override
    public List<Usuario> listarActivos() {
        return uR.listarActivos();
    }

    // HU47
    @Override
    public List<UsuarioDTCList> listarActivosConRol() {
        List<UsuarioDTCList> lista = new ArrayList<>();
        for (Object[] fila : uR.listarActivosConRolNative()) {
            UsuarioDTCList dto = new UsuarioDTCList();
            dto.setIdUsuario(((Number) fila[0]).longValue());
            dto.setNombre((String) fila[1]);
            dto.setApellido((String) fila[2]);
            dto.setCorreo((String) fila[3]);
            dto.setFechaRegistro(aLocalDateTime(fila[4]));
            dto.setActivo(true);
            dto.setIdRol(((Number) fila[5]).longValue());
            dto.setNombreRol((String) fila[6]);
            lista.add(dto);
        }
        return lista;
    }

    private LocalDateTime aLocalDateTime(Object valor) {
        if (valor instanceof Timestamp) {
            return ((Timestamp) valor).toLocalDateTime();
        }
        if (valor instanceof LocalDateTime) {
            return (LocalDateTime) valor;
        }
        return null;
    }

    @Override
    public void insert(Usuario usuario) {
        uR.save(usuario);
    }

    @Override
    public Optional<Usuario> listId(Long id) {
        return uR.findById(id);
    }

    // Spring Security busca al usuario por correo al hacer login
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        return uR.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
    }
    @Override
    public void delete(long id) {
        Usuario usuario = listId(id).orElse(null);
        if (usuario == null) {
            throw new ResourceNotFoundException("No se encontró el usuario con el ID: " + id);
        }
        uR.delete(usuario);
    }
}