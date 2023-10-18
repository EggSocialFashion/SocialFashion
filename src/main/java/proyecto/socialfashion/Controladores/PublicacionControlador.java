package proyecto.socialfashion.Controladores;



import java.time.LocalDateTime;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
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

import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Excepciones.Excepciones;

import proyecto.socialfashion.Repositorios.PublicacionRepositorio;

import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
public class PublicacionControlador {
    
    @Autowired
    PublicacionServicio publicacionServicio;
    
    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    
    
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
    
  
    /*
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/tendencias")
    public String Tendencias(ModelMap modelo){
    
        try {
            
           ArrayList<Publicacion>listaPorTendencias = (ArrayList<Publicacion>) publicacionServicio.listaPublicacionOrdenadasPorLikes();
            modelo.addAttribute("listaPorTendencias", listaPorTendencias);
            
            //HTML en el que se encuentran las tendencias
            return"tendencias.html";
            
        } catch (Excepciones ex) {
            modelo.put("Error", ex.getMessage());
            
            //HTML EN EL QUE SE INDIQUE ERROR DE TENDENCIAS
            return"index.html"
        }
    
    
    }
    
    

    */
    

     @GetMapping("/{id}")
    public String mostrarPublicacion(@PathVariable String id, Model model) {
        Optional<Publicacion> respuesta = publicacionRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            model.addAttribute("publicacion", publicacion);
            return "prueba_verPublicacion.html";
        } else {
            // Manejo de publicación no encontrada
            return "error"; // Puedes crear una página de error personalizada si lo deseas
        }
    }

    
    
    
    
    
}