package proyecto.socialfashion.Controladores;


import java.time.LocalDateTime;
import java.util.ArrayList;
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
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;
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
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private ComentarioServicio comentarioServicio;
    
    
    @GetMapping("/")
    public String publicaciones(ModelMap modelo){
        List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionGuest(); 
        modelo.addAttribute("publicacionesAlta", publicacionesAlta);
        //HTML con la pagina en donde se encuentran las publicaciones
        return"index.html";
    }
    
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacionesSocialFashion")
    public String publicacionesParaRegistados(HttpSession session, ModelMap modelo){
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionOrdenadasPorFechaAlta();
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("publicacionesAlta", publicacionesAlta);
        //HTML con la pagina en donde se encuentran las publicaciones
        return"index.html";
            
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrarPubli")
    public String registrarPublicacion(HttpSession session, ModelMap modelo){ 
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("usuario", logueado);
            
        return "publicaciones.html";
        
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/publicacion/registro")
    public String registro(@RequestParam(name ="titulo", required = false) String titulo, @RequestParam(name ="contenido", required = false) String contenido, @RequestParam(name ="categoria", required = false) String categoria, ModelMap modelo, MultipartFile archivo, HttpSession session){
        
        try {
            
          
           Usuario logueado = (Usuario) session.getAttribute("usuariosession");
           if(logueado  == null){
            return "redirect:login.html";
        }
           publicacionServicio.CrearPublicacion(archivo, titulo , contenido, LocalDateTime.now() , categoria, logueado);

           
            modelo.put("exito", "Publicacion registrada correctamente!");
            
            //REDIRECCION AL INDEX PRESENTADO
            return "redirect:/publicacionesSocialFashion";
        } catch (Excepciones ex) {
            
            modelo.put("Error", ex.getMessage());
            modelo.put("imagen", archivo);
            modelo.put("nombre", contenido);
            modelo.put("email", categoria);
            
            //Agg html en el que este el formulario. IDEM ANTERIOR o Que redireccione a una pagina error
            return "error.html";
        }
        
    }
    
 
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/tendencias")
    public String Tendencias(ModelMap modelo, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
         if(usuario == null){
            return "redirect:login.html";
        }
         
       
            
           List<Publicacion>listaPorTendencias = (ArrayList<Publicacion>) publicacionServicio.listaPublicacionOrdenadasPorLikes();
            modelo.addAttribute("listaPorTendencias", listaPorTendencias);
            
            //HTML en el que se encuentran las tendencias
            return"tendencias.html";

    
    }


    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacion/{id}")
    public String mostrarPublicacion(@PathVariable String id, Model model,HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if(usuario == null){
            return "redirect:login.html";
        }
        try {
            Optional<Publicacion> respuesta = publicacionServicio.buscarPublicacionPorId(id);
            
            if (respuesta.isPresent()) {
                Publicacion publicacion = respuesta.get();
                List<Comentario> comentarios = comentarioServicio.comentarioPorPublicacion(id);
                model.addAttribute("publicacion", publicacion);
                model.addAttribute("comentarios", comentarios);
                return "prueba_verPublicacion.html";
            } else {
                model.addAttribute("error", "Error en publicacion");
                return "error"; 
            }
        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("error", "Error al cargar la imagen");
            return "error.html";

        }

    }
 
}