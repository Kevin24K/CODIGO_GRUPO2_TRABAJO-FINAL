package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.EstadoCoincidencia;

import java.util.List;
import java.util.Optional;

public interface IEstadoCoincidenciaService {
    void insert(EstadoCoincidencia estadoCoincidencia);
    List<EstadoCoincidencia> list();
    public Optional<EstadoCoincidencia> listId(Long id);
    void delete(long id);
    public void update(EstadoCoincidencia estadoCoincidencia);
    List<EstadoCoincidencia> listPendientes();

}
