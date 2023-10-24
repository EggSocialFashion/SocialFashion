
package proyecto.socialfashion.Controladores;


import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;


import proyecto.socialfashion.Servicios.PublicacionServicio;


@Controller
@RequestMapping(value = "/likes", method = { RequestMethod.GET, RequestMethod.POST })
public class LikeControlador {
    
   /* @Autowired
    LikeServicio likeServicio;
    */
    @Autowired
    PublicacionServicio publicacionServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/GeneradoLikes/{idPublicacion}")
    public String generandoLike(HttpSession session, ModelMap modelo, @PathVariable String idPublicacion){
        
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if(usuario == null){
            return "redirect:login.html";
        }
        /*
        Publicacion publicacion = publicacionServicio.getOne(idPublicacion);
        likeServicio.crearLike(publicacion , usuario);*/
        
        return "redirect:/index.html";

    }
    
    
    
    
            
            
            
            
            
    
    
}
