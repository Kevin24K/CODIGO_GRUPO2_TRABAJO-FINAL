package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.Objeto;
import pe.edu.upc.demosi.dtos.ObjetoCategoriaDTCList;

import java.util.List;
import java.util.Optional;

public interface IObjetoService {
    void insert(Objeto objeto);
    List<Objeto> list();
    void delete(long id);
    List<Objeto> listarPorCategoria(long idCategoria);
    public Optional<Objeto> listId(Long id);
    List<Objeto> listarActivos();
    List<ObjetoCategoriaDTCList> listarConCategoria();
}
