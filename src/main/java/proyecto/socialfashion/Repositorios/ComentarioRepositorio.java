package proyecto.socialfashion.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import proyecto.socialfashion.Entidades.Comentario;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {
    
    @Query("SELECT c FROM Comentario c WHERE c.idPublicacion.idPublicacion = :idPublicacion AND c.estado = true")
    public List<Comentario> buscarComentarioPorPublicacion(@Param("idPublicacion") String idPublicacion);

    
    public List<Comentario> findByIdPublicacion_IdPublicacion(String idPublicacion);



}
