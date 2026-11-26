package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.EstadoReporte;
import pe.edu.upc.demosi.entities.Notificacion;


import java.util.List;
import java.util.Optional;

public interface IEstadoReporteService {
    void insert(EstadoReporte estadoReporte);
    List<EstadoReporte> list();
    void delete(long id);
    public void update(EstadoReporte estadoReporte);
    public Optional<EstadoReporte> listId(Long id);
}
