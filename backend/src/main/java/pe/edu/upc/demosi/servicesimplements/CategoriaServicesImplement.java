package pe.edu.upc.demosi.servicesimplements;
import org.springframework.stereotype.Service;
import pe.edu.upc.demosi.entities.Categoria;
import pe.edu.upc.demosi.exceptions.ResourceNotFoundException;
import pe.edu.upc.demosi.repositories.ICategoriaRepository;
import pe.edu.upc.demosi.servicesinterfaces.ICategoriaService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServicesImplement implements ICategoriaService {
    private final ICategoriaRepository cR;

    public CategoriaServicesImplement(ICategoriaRepository cR) {
        this.cR = cR;
    }

    @Override
    public List<Categoria> list() {
        return cR.findAll();
    }

    @Override
    public void insert(Categoria categoria) {
        cR.save(categoria);
    }

    @Override
    public void delete(long id) {
        Categoria categoria = listId(id).orElse(null);
        if (categoria == null) {
            throw new ResourceNotFoundException("No se encontró la categoría con el ID: " + id);
        }
        cR.delete(categoria);
    }
    @Override
    public void update(Long id, Categoria c) {
        Categoria categoria = listId(id).orElse(null);
        if (categoria != null) {
            categoria.setNombreCategoria(c.getNombreCategoria());
            categoria.setDescripcionCategoria(c.getDescripcionCategoria());
            categoria.setActivoCategoria(c.getActivoCategoria());

            cR.save(categoria);
        } else {
            throw new ResourceNotFoundException("No se encontró la categoría con el ID: " + id);
        }
    }
    @Override
    public Optional<Categoria> listId(Long id) {
        return cR.findById(id);
    }

}
