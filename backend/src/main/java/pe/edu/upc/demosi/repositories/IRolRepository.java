package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.Rol;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {
}
