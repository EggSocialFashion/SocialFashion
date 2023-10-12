package proyecto.socialfashion.Servicios;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Repositorios.ComentarioRepositorio;

@Service
public class ComentarioServicio {

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    @Transactional
    public void guardarComentario(Comentario comentario) {

        comentarioRepositorio.save(comentario);
    }
    
    @Transactional
    public void borrarComentario(String idComentario) {
        Optional<Comentario> comentarioOptional = comentarioRepositorio.findById(idComentario);
        if (comentarioOptional.isPresent()) {
            Comentario comentario = comentarioOptional.get();
            comentario.setEstado(false);
            comentarioRepositorio.save(comentario);
            
        }
    }
    @Transactional
    public Optional<Comentario> buscarComentarioPorId(String id) {

        return comentarioRepositorio.findById(id);
    }
    @Transactional
    public void cambiarEstado(String idUsuario) {
        Optional<Comentario> respuesta = comentarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {

            Comentario comentario = respuesta.get();

            if (comentario.getEstado() == true) {

                comentario.setEstado(false);

            } else if (comentario.getEstado() == false) {

                comentario.setEstado(true);
            }
        }

    }
     

}

