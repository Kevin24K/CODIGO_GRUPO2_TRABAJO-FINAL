package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.dtos.SumaSimilitudEstadoDTO;
import pe.edu.upc.demosi.entities.Coincidencia;

import java.util.List;

@Repository
public interface ICoincidenciaRepository extends JpaRepository<Coincidencia, Long> {
    @Query("SELECT new pe.edu.upc.demosi.dtos.SumaSimilitudEstadoDTO(" +
            "ec.nombreEC, COALESCE(SUM(c.porcentajeSimilitudC), 0.0)) " +
            "FROM Coincidencia c " +
            "JOIN c.estadoCoincidencia ec " +
            "GROUP BY ec.idEstadoCoincidencia, ec.nombreEC " +
            "ORDER BY ec.nombreEC ASC")
    List<SumaSimilitudEstadoDTO> obtenerSumaSimilitudPorEstado();
}
