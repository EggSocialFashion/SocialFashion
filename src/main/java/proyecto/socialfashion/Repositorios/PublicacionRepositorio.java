
package proyecto.socialfashion.Repositorios;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.socialfashion.Entidades.Publicacion;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, String> {

    @Query(value = "SELECT p FROM Publicacion p WHERE p.alta <= :fechaHoy AND p.estado = 1 ORDER BY p.alta DESC")
    public List<Publicacion> buscarPublicacionPorFechaDeAlta(@Param("fechaHoy") LocalDateTime fechaHoy);

    
}