package proyecto.socialfashion.Controladores;


import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;
import proyecto.socialfashion.Servicios.LikeServicio;


import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;


@Controller
@RequestMapping(value = "/likes", method = { RequestMethod.GET, RequestMethod.POST })
public class LikeControlador {
    
    @Autowired
    LikeServicio likeServicio;
 
    @Autowired
    PublicacionServicio publicacionServicio;
    
    @Autowired
    PublicacionRepositorio publicacionRepositorio;
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/GenerandoLikes/{idPublicacion}")
    public String generandoLike(@PathVariable String idPublicacion, HttpSession session, ModelMap modelo){
    
         Usuario logueado = (Usuario) session.getAttribute("usuariosession");
      
        try {

                Optional<Publicacion> optionalPublicacion = publicacionRepositorio.findById(idPublicacion);
                if (optionalPublicacion.isPresent()) {
                    Publicacion publicacion = optionalPublicacion.get();
                    likeServicio.crearLike(publicacion, logueado);
                }

            return "redirect:/publicacionesSocialFashion";
        }catch (Exception e) {
            modelo.addAttribute("error",e.getMessage());
            return "redirect:/publicacionesSocialFashion";
 
        }
    }
    
    @GetMapping("/checkLike")
    @ResponseBody
    public ResponseEntity<Boolean> checkLike(@RequestParam("publicacionId") String publicacionId,
                                                @RequestParam("usuarioId") String usuarioId) {
    
            boolean tieneLike = likeServicio.tieneLike(publicacionId, usuarioId);
            return ResponseEntity.ok(tieneLike);

            
}

    
    
            
            
            
            
            
    
    
}