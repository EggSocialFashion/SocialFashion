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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.socialfashion.Entidades.Reporte;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Estado;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Servicios.ReporteServicio;

@Controller
@RequestMapping("/admin")
public class AdminReportesControlador {

    @Autowired
    private ReporteServicio reporteServicio;

    // ver denuncias por usuario
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String reporteUsuarios(Model modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // controla que tengo permiso de administrador
        try {
            if (usuario.getRoles() == Roles.ADMIN) {
                // trae lista de reportes no desestimados filtrada por las que corresponden a
                // usuarios
                List<Reporte> reporteUsuario = reporteServicio.obtenerUsuariosReportadosPendientes();
                modelo.addAttribute("reporteUsuario", reporteUsuario);
                return "admin_usuario.html";
            } else {
                modelo.addAttribute("error", "Usuario no permitido");
                // sino tiene peremiso redirecciona al index
                return "index.html";
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    // ver denuncias por comentarios
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/comentarios")
    public String reporteComentarios(HttpSession session, ModelMap modelo) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // verifica que tenga roll admin
        try {
            if (usuario.getRoles() == Roles.ADMIN) {
                // trae lista de reportes no desestimados que sean de comentarios
                List<Reporte> reporteComentarios = reporteServicio.obtenerComentariosReportadosPendientes();
                modelo.addAttribute("reporteComentarios", reporteComentarios);
                return "admin_comentarios.html";
            } else {
                // sino tiene roll admin lo manda al index
                modelo.addAttribute("error", "Usuario no permitido");
                return "index.html";
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/publicaciones")
    public String reportePublicaciones(HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // verifica que tenga roll admin
        try {
            if (usuario.getRoles() == Roles.ADMIN) {
                // filtra por tipo publicacion y que no tengan estado desestimado
                List<Reporte> reportePublicaciones = reporteServicio.obtenerPublicacionesReportadasPendientes();
                modelo.addAttribute("reportePublicaciones", reportePublicaciones);
                return "admin_publicaciones.html";
            } else {
                // sino tiene roll admin lo redirecciona al index
                modelo.addAttribute("mensaje", "Usuario no permitido");
                return "index.html";
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/usuario")
    public String resultadoReporteUsuario(@RequestParam String estado,
            @RequestParam String idReporte,
            HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // verifica que tenga roll admin
        try {
            if (usuario.getRoles().equals(Roles.ADMIN)) {
                // pasa estado a mayuscula por si viene en minuscula desde el front
                estado = estado.toUpperCase();
                Optional<Reporte> reporteC = reporteServicio.buscarReportePorId(idReporte);
                // controla que exista el reporte
                if (reporteC.isPresent()) {
                    Reporte reporte = reporteC.get();
                    // verifica cual es la respuesta /aceptado o desestimado
                    if (Estado.ACEPTADO.name().equals(estado)) {
                        // devuleve true si salió todo bien
                        boolean resultadoA = reporteServicio.aceptarReporte(reporte);
                        if (resultadoA == true) {
                            modelo.addAttribute("exito", "ok");
                        } else {
                            modelo.addAttribute("error", "Ocurrio un error");
                        }
                    } else if (Estado.DESESTIMADO.name().equals(estado)) {
                        // devuleve true si salió todo bien
                        boolean resultadoA = reporteServicio.aceptarReporte(reporte);
                        if (resultadoA == true) {
                            modelo.addAttribute("exito", "ok");
                        } else {
                            modelo.addAttribute("error", "Ocurrio un error");
                        }

                    } else {
                        // cual quier otra opción que llegue del front da error
                        modelo.addAttribute("error", "Opcion no valida");
                    }
                } else {
                    modelo.addAttribute("error", "Reporte no encontrado");
                }
                return "redirect:/admin/usuarios";
            } else {
                // si no es admin lo manda al index
                return "redirect:index.html";
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/comentario")
    public String resultadoReporteComentario(@RequestParam String estado,
            @RequestParam String idReporte,
            HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            // verifica que sea administrador
            if (usuario.getRoles().equals(Roles.ADMIN)) {
                // paso estado a mayuscula por si viene mal del front
                estado = estado.toUpperCase();
                Optional<Reporte> reporteC = reporteServicio.buscarReportePorId(idReporte);
                // verifico que exista el reporte
                if (reporteC.isPresent()) {
                    Reporte reporte = reporteC.get();
                    // verifica por tipo de estado
                    if (Estado.ACEPTADO.name().equals(estado)) {
                        // devuelve true si todo salió bien
                        boolean resultadoA = reporteServicio.aceptarReporte(reporte);
                        if (resultadoA == true) {
                            modelo.addAttribute("exito", "ok");
                        } else {
                            modelo.addAttribute("error", "Ocurrio un error");
                        }
                    } else if (Estado.DESESTIMADO.name().equals(estado)) {
                        // devuelve true si todo salió bien
                        boolean resultadoA = reporteServicio.desestimarReporte(reporte);
                        if (resultadoA == true) {
                            modelo.addAttribute("exito", "Ok");
                        } else {
                            modelo.addAttribute("error", "Ocurrio un error");
                        }
                    } else {
                        // cualquier cosa que venga del front y no sea ACEPTADO O DESESTIMADO
                        modelo.addAttribute("error", "Opcion no valida");
                    }
                } else {
                    modelo.addAttribute("error", "Reporte no encontrado");
                }
                return "redirect:/admin/comentarios";
            } else {
                // sino es administrador lo manda al index
                return "redirect:index.html";
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/publicacion")
    public String resultadoReportePublicacion(@RequestParam String estado,
            @RequestParam String idReporte,
            HttpSession session, Model modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            // verifica que sea administrador
            if (usuario.getRoles().equals(Roles.ADMIN)) {
                // paso a mayuscula por si viene mal del front
                estado = estado.toUpperCase();
                Optional<Reporte> reporteC = reporteServicio.buscarReportePorId(idReporte);
                // verifico que exista el reporte
                if (reporteC.isPresent()) {
                    Reporte reporte = reporteC.get();
                    // analizo por estado
                    if (Estado.ACEPTADO.name().equals(estado)) {
                        // si sale todo bien devuelve true
                        boolean resultadoA = reporteServicio.aceptarReporte(reporte);
                        if (resultadoA == true) {
                            modelo.addAttribute("exito", "Ok");
                        } else {
                            modelo.addAttribute("error", "Ocurrio un error");
                        }
                    } else if (Estado.DESESTIMADO.name().equals(estado)) {
                        boolean resultadoA = reporteServicio.desestimarReporte(reporte);
                        if (resultadoA == true) {
                            modelo.addAttribute("exito", "Ok");
                        } else {
                            modelo.addAttribute("error", "Ocurrio un error");
                        }

                    } else {
                        // cualquier otro dato que venga del front se rechaza
                        modelo.addAttribute("error", "Opcion no valida");
                    }
                } else {
                    modelo.addAttribute("error", "Reporte no encontrado");
                }
                return "redirect:/admin/publicaciones";
            } else {
                // sino es administrador lo manda al index
                return "redirect:index.html";
            }
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }
}
