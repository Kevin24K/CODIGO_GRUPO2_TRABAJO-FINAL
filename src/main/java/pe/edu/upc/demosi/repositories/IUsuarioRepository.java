package pe.edu.upc.demosi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.demosi.entities.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);
    // HU46: usuarios activos (JPQL)
    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    List<Usuario> listarActivos();

    // HU47: usuarios activos con su rol (SQL nativo con JOIN)
    @Query(value = "SELECT u.id_usuario, u.nombre, u.apellido, u.correo, u.fecha_registro, r.id_rol, r.nombre_rol " +
            "FROM usuario u " +
            "INNER JOIN rol r ON u.id_rol = r.id_rol " +
            "WHERE u.activo = true", nativeQuery = true)
    List<Object[]> listarActivosConRolNative();
}