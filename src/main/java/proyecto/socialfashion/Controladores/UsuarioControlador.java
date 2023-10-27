package proyecto.socialfashion.Controladores;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Excepciones.Excepciones;
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
        try {

            if (usuarios == null) {
                modelo.addAttribute("error", "Ocurrió un error al cargar el listado");
            } else {
                modelo.addAttribute("usuarios", usuarios);
            }
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
        }
        return "usuario_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String mostrarPerfilUsuario(@PathVariable String id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:login.html";
        }
        try {
            Optional<Usuario> respuesta = usuarioServicio.buscarUsuarioPorId(id);

            if (respuesta.isPresent()) {
                Usuario usuarioPerfil = respuesta.get();
                model.addAttribute("usuarioPerfil", usuarioPerfil);
                System.out.println(usuarioPerfil);
                return "perfilDeUsuario.html";
            } else {
                model.addAttribute("error", "Error en perfil de usuario");
                return "error";
            }
        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("error", "Error al cargar el usuario");
            return "error.html";

        }

    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("Error", "Usuario o Contraseña invalidos!");
        }
        return "login.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo, HttpSession session, MultipartFile archivo) {

        try {
            usuarioServicio.registrar(archivo, nombre, email, password, password2);
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
    public String buscarpornombre(ModelMap modelo) {
        return "buscar_nombre.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/buscarnombre")
    public String buscarnombre(@RequestParam String nombre, ModelMap modelo) throws Excepciones {
        // Usuario usuario = usuarioRepositorio.buscarPorNombre(nombre);
        try {
            if (nombre.isEmpty()) {
                modelo.addAttribute("error", "Debe ingresar un nombre");
            }
            List<Usuario> usuariosNom = usuarioServicio.buscarUsuarioPorNombre(nombre);

            if (usuariosNom.isEmpty() || usuariosNom.size() == 0) {
                modelo.put("error", "El Usuario no ha sido encontrado.");
            } else {
                modelo.addAttribute("usuariosNom", usuariosNom);
                modelo.put("exito", "El Usuario ha sido encontrado.");
            }
            return "buscar_nombre.html";
        } catch (Excepciones ex) {
            modelo.put("error", ex.getMessage());
            // modelo.put("nombre", nombre);
            return "buscar_nombre.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{idUsuario}")
    public String modificarUsuario(@PathVariable String idUsuario, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario.getIdUsuario().toString().equals(idUsuario)) {
            modelo.put("usuario", usuarioServicio.getOne(idUsuario));
            return "usuario_modificar.html";
        } else {
            modelo.addAttribute("error", "Usuario Incorrecto");
            return "index.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{idUsuario}")
    public String modificar(MultipartFile archivo, @PathVariable String idUsuario, String nombre, String email,
            String password,
            String password2, Roles roles, ModelMap modelo) {
        try {
            usuarioServicio.actualizar(archivo, idUsuario, nombre, email, password, password2, roles);
            return "redirect:../lista";
        } catch (Excepciones ex) {
            modelo.put("error", ex.getMessage());
            return "index.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarRol/{idUsuario}")
    public String cambiarRol(@PathVariable String idUsuario, Model modelo) {
        try {
            usuarioServicio.cambiarRol(idUsuario);
            return "redirect:/usuarios/lista";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "redirect:/usuarios/lista";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarEstado/{idUsuario}")
    public String cambiarEstado(@PathVariable String idUsuario, Model modelo) {
        try {
            usuarioServicio.cambiarEstado(idUsuario);

            return "redirect:/usuarios/lista";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "redirect:/usuarios/lista";
        }
    }
}