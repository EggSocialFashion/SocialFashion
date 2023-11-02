package proyecto.socialfashion.Controladores;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Like;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Excepciones.Excepciones;
import proyecto.socialfashion.Servicios.ComentarioServicio;
import proyecto.socialfashion.Servicios.LikeServicio;
import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.ReporteServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
public class PublicacionControlador {

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;
    
    @Autowired 
    private LikeServicio likeServicio;

    @Autowired
    private ReporteServicio reporteServicios;


    @GetMapping("/")
    public String publicaciones(ModelMap modelo, HttpSession session) {
        List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionGuest();
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        List<Usuario> usuarios = usuarioServicio.diseniadores();
        modelo.addAttribute("usuarios",usuarios);
        modelo.addAttribute("publicacionesAlta", publicacionesAlta);
        modelo.addAttribute("logueado", logueado);
        // HTML con la pagina en donde se encuentran las publicaciones
        return "index.html";
    }
 

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacionesSocialFashion")
    public String publicacionesParaRegistados(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
         if(logueado == null){
            return "redirect:login.html";
        }
        List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionOrdenadasPorFechaAlta();
        List<Usuario> usuarios = usuarioServicio.diseniadores();
        List<Like> likes = likeServicio.likesUsuarios(logueado);        
        modelo.addAttribute("likes", likes); 
        modelo.addAttribute("usuarios",usuarios);
        modelo.addAttribute("logueado", logueado);
        modelo.addAttribute("publicacionesAlta", publicacionesAlta);
        // HTML con la pagina en donde se encuentran las publicaciones
        return "index.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrarPubli")
    public String registrarPublicacion(HttpSession session, ModelMap modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("logueado", logueado);

        return "publicaciones.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/publicacion/registro")
    public String registro(@RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "contenido", required = false) String contenido,
            @RequestParam(name = "categoria", required = false) String categoria, ModelMap modelo,
            MultipartFile archivo, HttpSession session) {

        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            publicacionServicio.CrearPublicacion(archivo, titulo, contenido, LocalDateTime.now(), categoria, logueado);


            modelo.put("exito", "Publicacion registrada correctamente!");

            // REDIRECCION AL INDEX PRESENTADO
            return "redirect:/publicacionesSocialFashion";
        } catch (Excepciones ex) {

            modelo.put("Error", ex.getMessage());
            modelo.put("imagen", archivo);
            modelo.put("nombre", contenido);
            modelo.put("email", categoria);

            // Agg html en el que este el formulario. IDEM ANTERIOR o Que redireccione a una
            // pagina error
            return "error.html";
        }

    }

    
 
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/tendencias")
    public String Tendencias(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:login.html";
        }    
        try {
            List<Object[]> publicacionesConLikeYComentarios = publicacionServicio.listaPublicacionOrdenadasPorLikes();
    
            // Ordenar la lista en orden descendente por la cantidad de "likes"
            Collections.sort(publicacionesConLikeYComentarios, new Comparator<Object[]>() {
                @Override
                public int compare(Object[] o1, Object[] o2) {
                    int likes1 = (int) o1[1];
                    int likes2 = (int) o2[1];
                    return Integer.compare(likes2, likes1);
                }
            });
    
            modelo.addAttribute("publicacionesConLikeYComentarios", publicacionesConLikeYComentarios);
            return "tendencias.html";
        } catch (Exception e) {
            modelo.addAttribute("error", "No hay publicaciones en tendencias ");
            return "tendencias.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacion/{id}")
    public String mostrarPublicacion(@PathVariable String id, HttpSession session, Model modelo) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        try {
            Optional<Publicacion> respuesta = publicacionServicio.buscarPublicacionPorId(id);
            if (respuesta.isPresent()) {
                Publicacion publicacion = respuesta.get();
                if (publicacion.isEstado() == true) {
                    List<Comentario> comentarios = comentarioServicio.comentarioPorPublicacion(id);
                    Integer totalLike = likeServicio.totalLike(id);
                    Integer totalComentarios = comentarioServicio.totalComentariosPublicacion(id);
                    modelo.addAttribute("logueado", logueado);
                    modelo.addAttribute("totalLike",totalLike);
                    modelo.addAttribute("totalComentarios", totalComentarios);
                    modelo.addAttribute("publicacion", publicacion);
                    modelo.addAttribute("comentarios", comentarios);
                } else {
                    modelo.addAttribute("error", "La publicacion no existe");
                }
            } else {
                modelo.addAttribute("error", "La publicacion no existe");
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
        return "prueba_verPublicacion.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/publicacion/borrar/{id}")
    public String borrarPublicacion(@PathVariable String id, Model modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");      
        try {
            Optional<Publicacion> respuesta = publicacionServicio.buscarPublicacionPorId(id);

            if (respuesta.isPresent()) {
                Publicacion publicacion = respuesta.get();
                if (publicacion.isEstado() == true) {
                    publicacionServicio.BajaPublicacion(id);
                } else {
                    modelo.addAttribute("error", "La publicacion no existe");
                }
            } else {
                modelo.addAttribute("error", "La publicacion no existe");
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

        return "redirect:/usuarios/perfil/"+ usuario.getIdUsuario() ;
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/estadisticas")
    public String estadisticasUsuario(HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        List<Object[]> listado = reporteServicios.estadisticaPorUsuario(usuario);
        modelo.addAttribute("listado", listado);
        return "estadisticas.html";
    }
   
}