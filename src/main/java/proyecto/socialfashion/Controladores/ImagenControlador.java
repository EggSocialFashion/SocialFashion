package proyecto.socialfashion.Controladores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    PublicacionServicio publicacionServicio;

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String idPublicacion){
        Publicacion publicacion = publicacionServicio.getOne(idPublicacion);        
        byte [] imagen= publicacion.getImagen().getContenido();        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);                        
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);    
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<byte[]> imagenPerfil(@PathVariable String idUsuario){
        Usuario usuario = usuarioServicio.getOne(idUsuario);        
        byte [] imagen= usuario.getImagen().getContenido();        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);                        
    return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
    
    }
    
    
    
    
}