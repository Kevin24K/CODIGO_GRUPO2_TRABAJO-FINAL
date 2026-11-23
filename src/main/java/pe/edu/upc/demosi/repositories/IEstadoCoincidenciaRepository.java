package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.EstadoCoincidencia;

import java.util.List;

@Repository
public interface IEstadoCoincidenciaRepository extends JpaRepository<EstadoCoincidencia, Long> {
    @Query(value = "SELECT c.* FROM estado_coincidencia c " +
            "INNER JOIN estado_coincidencia e ON c.id_estado_coincidencia = e.id_estado_coincidencia " +
            "WHERE e.nombre_estado_coincidencia = 'PENDIENTE'",
            nativeQuery = true)
    List<EstadoCoincidencia> findCoincidenciasPendientes();
}
