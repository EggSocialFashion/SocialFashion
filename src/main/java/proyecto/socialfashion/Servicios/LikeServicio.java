package proyecto.socialfashion.Servicios;

import java.util.ArrayList;
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

    @Autowired
    private UsuarioServicio usuarioServicio;
            
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
            List<Like>listaDeLikes = publicacionRepositorio.getReferenceById(publicacion.getIdPublicacion()).getLikes();
            listaDeLikes.add(like);
            publicacion.setLikes(listaDeLikes);
            
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
    // este es para la pagina de verPublicacion la cantidad de liks que tiene la publicación
    @Transactional
    public Integer totalLike(String idPublicacion){
       Integer respuesta=0;

        List<Like> likes = likeRepositorio.findAll();

        for (Like like : likes) {
            if(like.getEstado()==true&&like.getPublicacion().getIdPublicacion().equals(idPublicacion)){
                respuesta+=1;
            }
        }
     
        return respuesta;
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
    // este se utiliza para poner en el front en el carrusel si tiene like o no
    public boolean tieneLike (String idPublicacion, String idUsuario){
        boolean respuesta=false;

        List<Like> likes = likeRepositorio.findAll();
        List<Like> likeDepurado = new ArrayList<>();

        for (Like like : likes) {
            if(like.getEstado()==true&&like.getPublicacion().getIdPublicacion().equals(idPublicacion)
            &&like.getUsuario().getIdUsuario().equals(idUsuario)){
                likeDepurado.add(like);
            }
        }
     
        if(likeDepurado.size()==0){
            respuesta=false;
        }else{
            respuesta=true ;
        }
            
        return respuesta;
    }
    
}