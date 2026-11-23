package pe.edu.upc.demosi.servicesimplements;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.EstadoCoincidencia;
import pe.edu.upc.demosi.repositories.IEstadoCoincidenciaRepository;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoCoincidenciaService;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoCoincidenciaServicesImplement implements IEstadoCoincidenciaService {

    private final IEstadoCoincidenciaRepository eR;

    public EstadoCoincidenciaServicesImplement(IEstadoCoincidenciaRepository eR) {
        this.eR = eR;
    }

    @Override
    public List<EstadoCoincidencia> list() {
        return eR.findAll();
    }

    @Override
    public void insert(EstadoCoincidencia estadoCoincidencia) {
        eR.save(estadoCoincidencia);
    }

    @Override
    public Optional<EstadoCoincidencia> listId(Long id) {
        return eR.findById(id);
    }

    @Override
    public void delete(long id) {
        eR.deleteById(id);
    }

    @Override
    public List<EstadoCoincidencia> listPendientes() {
        return eR.findCoincidenciasPendientes(); // Asegúrate de que eR sea tu IEstadoCoincidenciaRepository inyectado
    }

}
