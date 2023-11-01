

package proyecto.socialfashion.Repositorios;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.socialfashion.Entidades.Like;




@Repository
public interface LikeRepositorio extends JpaRepository<Like, String>{    
    @Query("SELECT l FROM Like l WHERE l.publicacion.idPublicacion = :idPublicacion AND l.estado = 1")
    public List<Like> buscarLikePorPubli(@Param("idPublicacion")String idPublicacion);    
    @Query("SELECT l FROM Like l WHERE l.usuario.idUsuario = :idUsuario")
    public List<Like> buscarLikePorUsuario(@Param("idUsuario")String idUsuario);  
    @Query("SELECT l FROM Like l WHERE l.publicacion.idPublicacion = :idPublicacion AND l.usuario.idUsuario = :idUsuario")
    public Like buscarLikeExistente(@Param("idPublicacion")String idPublicacion,@Param("idUsuario")String idUsuario);    
    @Query("SELECT COUNT(l), l.publicacion.idPublicacion FROM Like l WHERE l.publicacion.idPublicacion = :idPublicacion AND l.estado = 1")
    public int cantidadDeLikesDeUnaPublicacion(@Param("idPublicacion")String idPublicacion);
    

}
