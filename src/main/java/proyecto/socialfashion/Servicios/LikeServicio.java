
package proyecto.socialfashion.Servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.socialfashion.Entidades.Like;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Repositorios.LikeRepositorio;
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;



@Service
public class LikeServicio {
    
    @Autowired
    LikeRepositorio likeRepositorio;
    
    @Autowired
    PublicacionRepositorio publicacionRepositorio;
            
    @Transactional()
    public void crearLike(Publicacion publicacion, Usuario usuario){
        Like like = new Like();
        boolean validacion = validarCreado(publicacion, usuario);
        
        if (validacion == true) {
           like = likeRepositorio.buscarLikeExistente(publicacion.getIdPublicacion(), usuario.getIdUsuario());
            ModificarLike(like.getIdLike());
        } else {
            like.setPublicacion(publicacion);
            like.setUsuario(usuario);
            like.setEstado(true);
            likeRepositorio.save(like);

        }
    }
    

    //Con este metodo modifico el estado del like para 'borrarlo o no'
    @Transactional()
    public void ModificarLike(String idLike){
        Like like = likeRepositorio.getOne(idLike);
        if (like.getEstado() == true) {
            like.setEstado(false);
        } else {
            like.setEstado(true);
        }
     
    }
   
    //Con este metodo traigo una List de todos los likes de la publicacion
    @Transactional(readOnly = true)
    public List<Like> cantidadDeLikes(String idPublicacion){
        List<Like> listaDeMeGustas = likeRepositorio.buscarLikePorPubli(idPublicacion);
        return listaDeMeGustas;
    }
    
    
    //Con este metodo valido si esta creado el like para no volver a crearlo al hacer el click
    @Transactional()
    public boolean validarCreado(Publicacion publicacion, Usuario usuario){
        List<Like> listaDeValidacion = likeRepositorio.findAll();
        
        for (Like like : listaDeValidacion) {
             if (like.getPublicacion().getIdPublicacion().equalsIgnoreCase(publicacion.getIdPublicacion()) && like.getUsuario().getIdUsuario().equalsIgnoreCase(usuario.getIdUsuario())) {
              return true;
             
             }
            
        }
        return false;

    }

    public List<Like>likesUsuarios (Usuario usuario){
      List<Like> listaDeLikesDeUnUsuario = likeRepositorio.buscarLikePorUsuario(usuario.getIdUsuario());
        
       return listaDeLikesDeUnUsuario;
    }
    
}
