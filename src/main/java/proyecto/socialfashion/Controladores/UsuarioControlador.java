package proyecto.socialfashion.Controladores;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Excepciones.Excepciones;
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;
import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    
    @Autowired
    PublicacionServicio publicacionServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_list.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam (required = false) String error, ModelMap modelo){
        if (error != null) {
            modelo.put("Error", "Usuario o Contraseña invalidos!");
        }
        return "login.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo, HttpSession session) {

        try {
            usuarioServicio.registrar(nombre, email, password, password2);
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            List<Publicacion> publicacionesAlta = publicacionServicio.listaPublicacionOrdenadasPorFechaAlta();
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("publicacionesAlta", publicacionesAlta);
            modelo.put("exito", "El Usuario ha sido registrado correctamente.");
           
            return "index.html";
        } catch (Excepciones ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
    }

     @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/buscarpornombre")
    public String buscarpornombre() {
        return "buscar_nombre.html";
    }
    
     @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/buscarnombre")
    public String buscarnombre(@RequestParam String nombre, ModelMap modelo) throws Excepciones{
        
        try {
            usuarioServicio.buscarUsuarioPorNombre(nombre, modelo);
            modelo.put("exito","El Usuario ha sido encontrado.");
            return "index.html";
        } catch (Excepciones ex) {
           modelo.put("error", ex.getMessage());
           modelo.put("nombre", nombre);
           return "buscar_nombre.html";
        }
        
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{idUsuario}")
    public String modificarUsuario(@PathVariable String idUsuario, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(idUsuario));

        return "usuario_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable String idUsuario, String nombre, String email, String password,
            String password2, Roles roles, ModelMap modelo) {
        try {
            usuarioServicio.actualizar(idUsuario, nombre, email, password, password2, roles);
            return "redirect:../lista";
        } catch (Excepciones ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificar.html";
        }

    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarRol/{idUsuario}")
    public String cambiarRol(@PathVariable String idUsuario) {
        usuarioServicio.cambiarRol(idUsuario);

        return "redirect:/usuarios/lista";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarEstado/{idUsuario}")
    public String cambiarEstado(@PathVariable String idUsuario) {
        usuarioServicio.cambiarEstado(idUsuario);

        return "redirect:/usuarios/lista";
    }
}