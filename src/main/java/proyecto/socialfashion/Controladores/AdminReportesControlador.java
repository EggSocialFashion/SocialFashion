package proyecto.socialfashion.Controladores;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Reporte;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Servicios.ComentarioServicio;
import proyecto.socialfashion.Servicios.PublicacionServicio;
import proyecto.socialfashion.Servicios.ReporteServicio;
import proyecto.socialfashion.Servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminReportesControlador {

    private ReporteServicio reporteServicio;
    private UsuarioServicio usuarioServicio;
    private ComentarioServicio comentarioServicio;
    private PublicacionServicio publicacionServicio;

    @Autowired
    public AdminReportesControlador(ReporteServicio reporteServicio, UsuarioServicio usuarioServicio,
            ComentarioServicio comentarioServicio, PublicacionServicio publicacionServicio) {
        this.reporteServicio = reporteServicio;
        this.usuarioServicio = usuarioServicio;
        this.comentarioServicio = comentarioServicio;
        this.publicacionServicio = publicacionServicio;

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String reporteUsuarios(ModelMap modelo,HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario.getRoles() == Roles.ADMIN) {
            List<Reporte> reporteUsuario = reporteServicio.obtenerUsuariosReportadosPendientes();
            modelo.addAttribute("Reporte Usuarios", reporteUsuario);
            return "adminUsuarios";
        } else {
            modelo.addAttribute("mensaje", "Usuario no permitido");
            return "index.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/comentarios")    public String reporteComentarios(HttpSession session, ModelMap modelo) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario.getRoles() == Roles.ADMIN) {
            List<Reporte> reporteComentarios = reporteServicio.obtenerComentariosReportadosPendientes();
            modelo.addAttribute("Reporte Comentarios", reporteComentarios);
            return "adminComentarios";
        } else {
            modelo.addAttribute("mensaje", "Usuario no permitido");
            return "index.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/publicaciones")
    public String reportePublicaciones(HttpSession session, ModelMap modelo, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario.getRoles() == Roles.ADMIN) {
            List<Reporte> reportePublicaciones = reporteServicio.obtenerPublicacionesReportadasPendientes();
            modelo.addAttribute("reporte Publicaciones", reportePublicaciones);
            return "adminPublicaciones";
        } else {
            modelo.addAttribute("mensaje", "Usuario no permitido");
            return "index.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/usuarios")
    public String resultadoReporteUsuario(@PathVariable String estado,
            @PathVariable String idObjeto,
            @PathVariable String idReporte,
            HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        Optional<Usuario> respuestaU = usuarioServicio.buscarUsuarioOptionalId(idObjeto);
        if (respuestaU.isPresent()) {
            Usuario usuario2 = respuestaU.get();
            String mensajeValidacion = reporteServicio.procesarReporte(estado, idObjeto, idReporte, usuario, modelo, usuario2);
            modelo.addAttribute("mensaje", mensajeValidacion);
        } else {
            modelo.addAttribute("mensaje", "Comentario no encontrado");
            return "admin/usuarios";
        }
                    return "admin/usuarios";

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/comentario")
    public String resultadoReporteComentario(@PathVariable String estado,
            @PathVariable String idObjeto,
            @PathVariable String idReporte,
            HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        Optional<Comentario> respuestaC = comentarioServicio.buscarComentarioPorId(idObjeto);
        if (respuestaC.isPresent()) {
            Comentario comentario = respuestaC.get();
            String mensajeValidacion = reporteServicio.procesarReporte(estado, idObjeto, idReporte, usuario, modelo, comentario);
            modelo.addAttribute("mensaje", mensajeValidacion);
        } else {
            modelo.addAttribute("mensaje", "Comentario no encontrado");
            return "admin/comentario";
        }
        return "admin/comentario";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/publicacion")
    public String resultadoReportePublicacion(@PathVariable String estado,
            @PathVariable String idObjeto,
            @PathVariable String idReporte,
            HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        Optional<Publicacion> respuestaP = publicacionServicio.buscarPublicacionPorId(idObjeto);
        if (respuestaP.isPresent()) {
            Publicacion publicacion = respuestaP.get();
            String mensajeValidacion = reporteServicio.procesarReporte(estado, idObjeto, idReporte, usuario, modelo, publicacion );
            modelo.addAttribute("mensaje", mensajeValidacion);
        }else{
            modelo.addAttribute("mensaje", "Publicacion no encontrada");
            return "admin/publicacion";
        }
    return "admin/publicacion";
    }
}