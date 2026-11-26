package pe.edu.upc.demosi.servicesinterfaces;

import pe.edu.upc.demosi.entities.Reporte;
import pe.edu.upc.demosi.dtos.TotalDTO;
import java.util.List;
import java.util.Optional;

public interface IReporteService {
    void insert(Reporte reporte);
    List<Reporte> list();
    void delete(long id);
    public Optional<Reporte> listId(Long id);
    List<Reporte> listarPorUsuario(long idUsuario);
    List<Reporte> listarPorEstado(long idEstadoReporte);
    public List<Reporte> listActivos();
    List<TotalDTO> getTotalByCategory();
    void update(Reporte reporte);
}
