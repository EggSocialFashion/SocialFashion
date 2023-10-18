package proyecto.socialfashion.Controladores;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;
import proyecto.socialfashion.Servicios.ComentarioServicio;
import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping("/")
public class ComentarioControlador {

    private ComentarioServicio comentarioServicio;
    private PublicacionRepositorio publicacionRepositorio;
    private PublicacionServicio publicacionServicio;
    private UsuarioServicio usuarioServicio;

    @Autowired
    public ComentarioControlador(ComentarioServicio comentarioServicio,
            PublicacionRepositorio publicacionRepositorio,
            PublicacionServicio publicacionServicio,UsuarioServicio usuarioServicio) {
        this.comentarioServicio = comentarioServicio;
        this.publicacionRepositorio = publicacionRepositorio;
        this.publicacionServicio = publicacionServicio;
        this.usuarioServicio = usuarioServicio;
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/publicacion/{idPublicacion}/comentario")
    public String guardarComentario(
            @PathVariable String idPublicacion,
            @RequestParam("texto") String texto,Model modelo,HttpSession session) {

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
           
       
        try {
            Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);
            if (respuesta.isPresent()) {
                Publicacion publicacion = respuesta.get();
                Comentario comentario = new Comentario(texto, usuario, publicacion);
                comentarioServicio.guardarComentario(comentario);
                modelo.addAttribute("mensaje", "Comentario guardado exitosamente");
            } else {
                modelo.addAttribute("mensaje", "Publicación inexistente");
                return "prueba_verPublicacion.html";
            }
        } catch (Exception e) {
            modelo.addAttribute("mensaje", e.getMessage());
            e.printStackTrace();
        }
         
        return "prueba_verPublicacion.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/comentario/{idComentario}")
    public String borrarComentario(
            @PathVariable String idComentario,Model modelo,HttpSession session) {

        try {
            Optional<Comentario> respuesta = comentarioServicio.buscarComentarioPorId(idComentario);
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            if (respuesta.isPresent()) {
                Comentario comentario = respuesta.get();
                if (comentario.getIdUsuario().toString() == usuario.getIdUsuario().toString()) {
                    comentarioServicio.borrarComentario(comentario.getIdComentario());
                    modelo.addAttribute("mensaje", "Comentario borrado exitosamente");
                    return "index.html";
                } else {
                    modelo.addAttribute("mensaje", "Usuario Incorrecto");
                    return "index.html";
                }
            } else {
                modelo.addAttribute("mensaje", "Comentario inexistente");
                return "index.html";
            }
        } catch (Exception e) {
            modelo.addAttribute(e.getMessage());
            return "index.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacion/comentarios/{idPublicacion}")
    public String publicacionComentarios(@PathVariable String idPublicacion,ModelMap modelo, Model model) {

        Optional<Publicacion> resultado = publicacionServicio.buscarPublicacionPorId(idPublicacion);
        if (resultado.isPresent()) {
            List<Comentario> comentarios = comentarioServicio.comentarioPorPublicacion(idPublicacion);
            modelo.addAttribute("comentariosPorPublicacion", comentarios);
        } else {
            model.addAttribute("mensaje", "Publicación no encotnrada");
        }
        return "publicacion";
    }

}
