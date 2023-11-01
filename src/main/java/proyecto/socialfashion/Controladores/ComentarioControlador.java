package proyecto.socialfashion.Controladores;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;
import proyecto.socialfashion.Servicios.ComentarioServicio;
import proyecto.socialfashion.Servicios.PublicacionServicio;

@Controller
@RequestMapping("/")
public class ComentarioControlador {

    @Autowired
    private ComentarioServicio comentarioServicio;
    @Autowired
    private PublicacionRepositorio publicacionRepositorio;
    @Autowired
    private PublicacionServicio publicacionServicio;

    // guardar un comentario
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/publicacion/guardar/{idPublicacion}")
    public String guardarComentario(
            @PathVariable String idPublicacion,
            @RequestParam("texto") String texto, Model modelo, HttpSession session) {
        // se busca usuario para gurdar en la variable
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // se controla si est alogueado, sino manda a loqueo
        if (usuario == null) {
            return "loguin.html";
        }

        try {
            // verifica que exista la publicacion
            Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);
            if (respuesta.isPresent()) {
                // se busca la publicación si existe
                Publicacion publicacion = respuesta.get();
                // se crea comentrio
                Comentario comentario = new Comentario(texto, usuario, publicacion);
                // se guarda comentario
                comentarioServicio.guardarComentario(comentario);

            } else {
                modelo.addAttribute("error", "Publicación inexistente");

            }
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/publicacion/" + idPublicacion;
    }

    // borrar un comentario
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/comentario/borrar/{idComentario}")
    public String borrarComentario(
            @PathVariable String idComentario, Model modelo, HttpSession session) {

        try {

            // para buscar si existe el comentario
            Optional<Comentario> respuesta = comentarioServicio.buscarComentarioPorId(idComentario);
            // se verifica usuario
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            
            if (respuesta.isPresent()) {
                // se obtiene el comentario a borrar
                Comentario comentario = respuesta.get();
                // se compara que el usuario que quiere borrarlo sea el mismo que lo creo
                if (comentario.getIdUsuario().getIdUsuario().toString().equals(usuario.getIdUsuario().toString()) || Roles.ADMIN.name().equals(usuario.getRoles().toString())){
                    // se llama al servicio de borrar comentario
                    comentarioServicio.borrarComentario(comentario.getIdComentario());
                    modelo.addAttribute("exito", "Comentario borrado exitosamente");
                } else {
                    // se informa que el usuario no es correto si no son iguales
                    modelo.addAttribute("error", "Usuario Incorrecto");
                }
                // se redirecciona a la publicación
                return "redirect:/publicacion/" + comentario.getIdPublicacion().getIdPublicacion();
            } else {
                modelo.addAttribute("error", "Comentario inexistente");
                return "index.html";
            }
       
} catch (Exception e) {
        modelo.addAttribute(e.getMessage());
        return "index.html";
    }

    }
    // buscar lista de comentarios
      

 
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacion/comentarios/{idPublicacion}")
    public String publicacionComentarios(@PathVariable String idPublicacion, Model modelo) {
        try {
            // se busca si exsite la publicacipon
            Optional<Publicacion> resultado = publicacionServicio.buscarPublicacionPorId(idPublicacion);
            if (resultado.isPresent()) {
                // si existe se hace una lista de ocmentarios de esa publicación
                List<Comentario> comentarios = comentarioServicio.comentarioPorPublicacion(idPublicacion);
                modelo.addAttribute("comentariosPorPublicacion", comentarios);
            } else {
                // si la publicación no existe se infroma
                modelo.addAttribute("error", "Publicación no encotnrada");
            }
            return "publicacion";
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    // buscar un comentario
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/comentario/{idComentario}")
    public String buscarComentario(@PathVariable String idComentario, Model modelo, HttpSession session) {
        
        try {
            // se busca si exsite la publicacipon
            Optional<Comentario> resultado = comentarioServicio.buscarComentarioPorId(idComentario);
            if (resultado.isPresent()) {
                // si existe se trae el comentario
                Comentario comentario = resultado.get();
                modelo.addAttribute("comentario", comentario);
            } else {
                // si la publicación no existe se infroma
                modelo.addAttribute("error", "No se encuentra comentario");
            }
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "comentarios.html";

    }

}