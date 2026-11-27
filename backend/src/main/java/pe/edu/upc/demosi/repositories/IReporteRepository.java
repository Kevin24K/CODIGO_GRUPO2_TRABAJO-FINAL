package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.Reporte;

import java.util.List;

@Repository
public interface IReporteRepository extends JpaRepository<Reporte, Long> {
    @Query("SELECT r FROM Reporte r WHERE r.activo = true")// HU50: Ejecutar consulta JPQL findReportesActivos()
    List<Reporte> findReportesActivos();
    List<Reporte> findByUsuario_IdUsuario(Long idUsuario);
    List<Reporte> findByEstadoReporte_IdEstadoReporte(Long idEstadoReporte);
    // 1. Total por categoría (para TotalDTO)
    @Query(value = "SELECT c.nombre_categoria, COUNT(r.id_reporte) " +
            "FROM categoria c " +
            "INNER JOIN objeto o ON c.id_categoria = o.id_categoria " +
            "INNER JOIN reporte r ON o.id_objeto = r.id_objeto " +
            "WHERE r.activo = true " +
            "GROUP BY c.nombre_categoria", nativeQuery = true)
    List<Object[]> getTotalByCategory();
}
