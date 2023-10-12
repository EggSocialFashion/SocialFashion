
package proyecto.socialfashion.Controladores;

import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Excepciones.Excepciones;

import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping("/publicacion")
public class PublicacionControlador {
    
    @Autowired
    PublicacionServicio publicacionServicio;
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    
    @GetMapping("/publicaciones")
    public String publicaciones(ModelMap modelo){
        /*try {*/
            ArrayList<Publicacion> publicacionesAlta = (ArrayList<Publicacion>) publicacionServicio.listaPublicacionGuest();
            modelo.addAttribute("publicacionesAlta", publicacionesAlta);
            //HTML con la pagina en donde se encuentran las publicaciones
            return"index.html";
        /*} catch (Excepciones ex) {
            modelo.put("Error", ex.getMessage());
            //HTML en donde se se trabaje el error
            return"error.html";
        }*/
    }
    
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/publicacionesSocialFashion")
    public String publicacionesParaRegistados(HttpSession session, ModelMap modelo){
        /*try {*/
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("usuario", logueado);
            ArrayList<Publicacion> publicacionesAlta = (ArrayList<Publicacion>) publicacionServicio.listaPublicacionOrdenadasPorFechaAlta();
            modelo.addAttribute("publicacionesAlta", publicacionesAlta);
            //HTML con la pagina en donde se encuentran las publicaciones
            return"index.html";
            /*
        } catch (Excepciones ex) {
            modelo.put("Error", ex.getMessage());
            //HTML en donde se se trabaje el error
            return"error.html";
        }
            */
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/registrarPubli")
    public String registrarPublicacion(HttpSession session, ModelMap modelo){ 
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("usuario", logueado);
        
        return "publicaciones.html";
        
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/registro")
    public String registro(@RequestParam String titulo, @RequestParam String contenido, @RequestParam("categoria") String categoria, ModelMap modelo, MultipartFile archivo, HttpSession session){
        
        try {
            
           Usuario logueado = (Usuario) session.getAttribute("usuariosession");
           publicacionServicio.CrearPublicacion(archivo, titulo , contenido,new Date() , categoria, logueado);

           
            modelo.put("exito", "Publicacion registrada correctamente!");
            
            //Agg html en el que este el formulario. IDEM ANTERIOR
             return"index.html";
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
            return"";
            
        } catch (Excepciones ex) {
            modelo.put("Error", ex.getMessage());
            
            //HTML EN EL QUE SE INDIQUE ERROR DE TENDENCIAS
            return""
        }
    
    
    }
    
    */
    
    
    
    
    
    
    
}