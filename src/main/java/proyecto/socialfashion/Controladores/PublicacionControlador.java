package proyecto.socialfashion.Controladores;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Excepciones.Excepciones;
import proyecto.socialfashion.Servicios.ComentarioServicio;
import proyecto.socialfashion.Servicios.PublicacionServicio;
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
        List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionOrdenadasPorFechaAlta();
        List<Usuario> usuarios = usuarioServicio.diseniadores();
        modelo.addAttribute("usuario", logueado);
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
        modelo.addAttribute("usuario", logueado);

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

    /*
     * @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
     * 
     * @GetMapping("/tendencias")
     * public String Tendencias(ModelMap modelo){
     * 
     * try {
     * 
     * ArrayList<Publicacion>listaPorTendencias = (ArrayList<Publicacion>)
     * publicacionServicio.listaPublicacionOrdenadasPorLikes();
     * modelo.addAttribute("listaPorTendencias", listaPorTendencias);
     * 
     * //HTML en el que se encuentran las tendencias
     * return"tendencias.html";
     * 
     * } catch (Excepciones ex) {
     * modelo.put("Error", ex.getMessage());
     * 
     * //HTML EN EL QUE SE INDIQUE ERROR DE TENDENCIAS
     * return"index.html"
     * }
     * 
     * 
     * }
     * 
     * 
     */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacion/{id}")
    public String mostrarPublicacion(@PathVariable String id, Model modelo) {
      
        try {
            Optional<Publicacion> respuesta = publicacionServicio.buscarPublicacionPorId(id);

            if (respuesta.isPresent()) {
                Publicacion publicacion = respuesta.get();
                if (publicacion.isEstado() == true) {
                    List<Comentario> comentarios = comentarioServicio.comentarioPorPublicacion(id);
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
    public String borrarPublicacion(@PathVariable String id, Model modelo) {
      
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
        return "usuario_perfil.html";
    }
    /*
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/filtrarPorTipo")
    public String filtrarPorTipo(@RequestParam(name = "tipo", required = false) List<String> tipos,  Model model) {
        
        try {
            if (tipos.size() == 0 || tipos.isEmpty() || tipos == null) {
                model.addAttribute("error", "No se encontraron publicaciones");
                return "index.html";
            }
            List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionPorTipo(tipos);
            if (publicacionesAlta.size() == 0 || publicacionesAlta.isEmpty() || publicacionesAlta == null) {
                model.addAttribute("error", "No se encontraron publicaciones");
                return "index.html";
            } else {
                model.addAttribute("publicacionesAlta", publicacionesAlta);
                return "index.html";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "index.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/filtrarPorDiseniador")
    public String filtrarPorTipoDiseniador(@RequestParam(name = "usuarios", required = false) List<String> usuarios, 
                Model model){
        
        try {
            if (usuarios.size() == 0 || usuarios.isEmpty() || usuarios == null) {
                model.addAttribute("error", "No se encontraron publicaciones");
                return "index.html";
            }
            List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionPorDiseniador(usuarios);
            if (publicacionesAlta.size() == 0 || publicacionesAlta.isEmpty() || publicacionesAlta == null) {
                model.addAttribute("error", "No se encontraron publicaciones");
                return "index.html";
            } else {
                model.addAttribute("publicacionesAlta", publicacionesAlta);
                return "index.html";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "index.html";
        }

    } 
 */
}