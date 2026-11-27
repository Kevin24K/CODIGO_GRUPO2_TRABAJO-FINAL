package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.DispositivoPush;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDispositivoPushRepository extends JpaRepository<DispositivoPush, Long> {
    List<DispositivoPush> findByUsuario_IdUsuario(Long idUsuario);
    Optional<DispositivoPush> findByTokenFcm(String tokenFcm);
}
