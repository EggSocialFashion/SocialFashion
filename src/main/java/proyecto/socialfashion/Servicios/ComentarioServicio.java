package proyecto.socialfashion.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Repositorios.ComentarioRepositorio;

@Service
public class ComentarioServicio {

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    @Autowired
    private ReporteServicio reporteServicio;
    

  
    @Transactional
    public void guardarComentario(Comentario comentario) {
        Comentario coment = comentario;
        coment.setEstado(true);
        comentarioRepositorio.save(coment);
    }
    
    @Transactional
    public void borrarComentario(String idComentario) {
        Optional<Comentario> comentarioOptional = comentarioRepositorio.findById(idComentario);
        if (comentarioOptional.isPresent()) {
            Comentario comentario = comentarioOptional.get();
            comentario.setEstado(false);
            comentarioRepositorio.save(comentario);
            reporteServicio.bajaComentarioReporte(comentario);
        }
        
    }
    

        @Transactional
        public Optional<Comentario> buscarComentarioPorId(String idComentario) {
            Optional<Comentario> comentario = comentarioRepositorio.findById(idComentario);

            return comentario;
        }
    
    @Transactional
    public List<Comentario> comentarioPorPublicacion(String idPublicacion){
        Publicacion publicacion = new Publicacion();
        publicacion.setIdPublicacion(idPublicacion);
        
        List<Comentario> comentarios = comentarioRepositorio.buscarComentarioPorPublicacion(idPublicacion);
        System.out.println(comentarios.size());
        return comentarios;
    }

    @Transactional
    public Integer totalComentariosPublicacion(String idPublicacion){
    
        List<Comentario> comentarios = comentarioRepositorio.buscarComentarioPorPublicacion(idPublicacion);
        List<Comentario> comentarioDepurado = new ArrayList<>();

        for (Comentario comentario : comentarios) {
            if(comentario.getIdPublicacion().getIdPublicacion().toString().equals(idPublicacion) 
                && comentario.getEstado()==true){
                comentarioDepurado.add(comentario);

            }
            
        }


        return comentarioDepurado.size();

    }

}
    
