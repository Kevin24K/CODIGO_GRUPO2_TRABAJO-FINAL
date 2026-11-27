package pe.edu.upc.demosi.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.Coincidencia;
import pe.edu.upc.demosi.entities.Reporte;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.repositories.IReporteRepository;
import pe.edu.upc.demosi.servicesinterfaces.IReporteService;
import pe.edu.upc.demosi.dtos.TotalDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteServicesImplement implements IReporteService {

    private final IReporteRepository rR;

    public ReporteServicesImplement(IReporteRepository rR) {
        this.rR = rR;
    }

    @Override
    public List<Reporte> list() {
        return rR.findAll();
    }

    @Override
    public void insert(Reporte reporte) {
        rR.save(reporte);
    }

    @Override
    public Optional<Reporte> listId(Long id) {
        return rR.findById(id);
    }

    @Override
    public void delete(long id) {
        Reporte reporte = listId(id).orElse(null);
        if (reporte == null) {
            throw new ResourceNotFoundException("No se encontró el reporte con el ID: " + id);
        }
        rR.delete(reporte);
    }

    @Override
    public List<Reporte> listarPorUsuario(long idUsuario) {
        return rR.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    public List<Reporte> listarPorEstado(long idEstadoReporte) {
        return rR.findByEstadoReporte_IdEstadoReporte(idEstadoReporte);
    }
    @Override
    public List<Reporte> listActivos() {
        return rR.findReportesActivos(); // Asegúrate de que rR sea tu IReporteRepository inyectado
    }
    @Override
    public List<TotalDTO> getTotalByCategory() {
        List<Object[]> lista = rR.getTotalByCategory();
        List<TotalDTO> listaDTO = new ArrayList<>();

        for (Object[] columna : lista) {
            TotalDTO dto = new TotalDTO();
            dto.setNombreCategoria(columna[0].toString());
            dto.setQuantityTotal(Double.parseDouble(columna[1].toString()));
            listaDTO.add(dto);
        }
        return listaDTO;
    }
    @Override
    public void update(Reporte reporte) {
        rR.save(reporte);
    }
}
