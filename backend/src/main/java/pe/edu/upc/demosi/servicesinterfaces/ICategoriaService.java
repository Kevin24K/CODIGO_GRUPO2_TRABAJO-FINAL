package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {
    void insert(Categoria categoria);
    List<Categoria> list();
    void delete(long id);
    Optional<Categoria> listId(Long id);
    void update(Long id, Categoria c);
}
