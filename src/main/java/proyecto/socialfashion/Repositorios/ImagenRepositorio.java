package proyecto.socialfashion.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.socialfashion.Entidades.Imagen;


@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, String> {
    
}
