package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.EstadoReporte;

@Repository
public interface IEstadoReporteRepository extends JpaRepository<EstadoReporte, Long> {
}
