package pe.edu.upc.demosi.servicesimplements;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.Coincidencia;
import pe.edu.upc.demosi.entities.Objeto;

import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.repositories.IObjetoRepository;
import pe.edu.upc.demosi.servicesinterfaces.IObjetoService;
import pe.edu.upc.demosi.dtos.ObjetoCategoriaDTCList;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service
public class ObjetoServicesImplement implements IObjetoService {
    private final IObjetoRepository oR;

    public ObjetoServicesImplement(IObjetoRepository oR) {
        this.oR = oR;
    }

    @Override
    public List<Objeto> list() {
        return oR.findAll();
    }

    @Override
    public List<Objeto> listarActivos() {
        return oR.listarActivos();
    }

    // HU49
    @Override
    public List<ObjetoCategoriaDTCList> listarConCategoria() {
        List<ObjetoCategoriaDTCList> lista = new ArrayList<>();
        for (Object[] fila : oR.listarConCategoriaNative()) {
            ObjetoCategoriaDTCList dto = new ObjetoCategoriaDTCList();
            dto.setIdObjeto(((Number) fila[0]).longValue());
            dto.setNombreObjeto((String) fila[1]);
            dto.setDescripcionObjeto((String) fila[2]);
            dto.setColorObjeto((String) fila[3]);
            dto.setMarcaObjeto((String) fila[4]);
            dto.setIdCategoria(((Number) fila[5]).longValue());
            dto.setNombreCategoria((String) fila[6]);
            lista.add(dto);
        }
        return lista;
    }

    @Override
    public void insert(Objeto objeto) {
        oR.save(objeto);
    }

    @Override
    public void delete(long id) {
        Objeto objeto = listId(id).orElse(null);
        if (objeto == null) {
            throw new ResourceNotFoundException("No se encontró el objeto con el ID: " + id);
        }
        oR.delete(objeto);
    }

    @Override
    public Optional<Objeto> listId(Long id) {
        return oR.findById(id);
    }

    @Override
    public List<Objeto> listarPorCategoria(long idCategoria) {
        return oR.findByCategoria_IdCategoria(idCategoria);
    }

}
