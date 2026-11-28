package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.Objeto;

import java.util.List;

@Repository
public interface IObjetoRepository extends JpaRepository<Objeto, Long> {
    List<Objeto> findByCategoria_IdCategoria(Long idCategoria);
    // HU48: objetos activos (JPQL)
    @Query("SELECT o FROM Objeto o WHERE o.activoObjeto = true")
    List<Objeto> listarActivos();

    // HU49: objetos con su categoría (SQL nativo con JOIN)
    @Query(value = "SELECT o.id_objeto, o.nombre_objeto, o.descripcion_objeto, o.color_objeto, o.marca_objeto, " +
            "c.id_categoria, c.nombre_categoria " +
            "FROM objeto o " +
            "INNER JOIN categoria c ON o.id_categoria = c.id_categoria", nativeQuery = true)
    List<Object[]> listarConCategoriaNative();
}
