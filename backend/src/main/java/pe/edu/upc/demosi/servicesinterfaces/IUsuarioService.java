package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.Usuario;
import pe.edu.upc.demosi.dtos.UsuarioDTCList;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    void insert(Usuario usuario);
    List<Usuario> list();
    Optional<Usuario> listId(Long id);
    List<Usuario> listarActivos();
    List<UsuarioDTCList> listarActivosConRol();
    void delete(long id);
}