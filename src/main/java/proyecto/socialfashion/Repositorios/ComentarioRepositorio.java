package proyecto.socialfashion.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.socialfashion.Entidades.Comentario;


@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {

}
