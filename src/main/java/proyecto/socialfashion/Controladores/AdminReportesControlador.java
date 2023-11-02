package proyecto.socialfashion.Controladores;
import java.util.List;
import java.util.Map;
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
import proyecto.socialfashion.Enumeraciones.Estado;
import proyecto.socialfashion.Servicios.ReporteServicio;

@Controller
@RequestMapping("/admin")
public class AdminReportesControlador {

    @Autowired
    private ReporteServicio reporteServicio;

    // ver denuncias por usuario
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String reporteUsuarios(Model modelo) {
        
        try {
            
                // trae lista de reportes no desestimados filtrada por las que corresponden a
                // usuarios
                List<Reporte> reporteUsuario = reporteServicio.obtenerUsuariosReportadosPendientes();
                modelo.addAttribute("reporteUsuario", reporteUsuario);
                return "admin_usuario.html";
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    // ver denuncias por comentarios
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/comentarios")
    public String reporteComentarios( ModelMap modelo) {

        try {
            
                // trae lista de reportes no desestimados que sean de comentarios
                List<Reporte> reporteComentarios = reporteServicio.obtenerComentariosReportadosPendientes();
                modelo.addAttribute("reporteComentarios", reporteComentarios);
                return "admin_comentarios.html";
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/publicaciones")
    public String reportePublicaciones(Model modelo) {

        try {
            
                // filtra por tipo publicacion y que no tengan estado desestimado
                List<Reporte> reportePublicaciones = reporteServicio.obtenerPublicacionesReportadasPendientes();
                modelo.addAttribute("reportePublicaciones", reportePublicaciones);
                return "admin_publicaciones.html";
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/usuario")
    public String resultadoReporteUsuario(@RequestParam String estado,
            @RequestParam String idReporte,Model modelo) {

        try {
            
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
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/comentario")
    public String resultadoReporteComentario(@RequestParam String estado,
            @RequestParam String idReporte,Model modelo) {
        try {
         
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
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/publicacion")
    public String resultadoReportePublicacion(@RequestParam String estado,
            @RequestParam String idReporte,Model modelo) {
       
        try {
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
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/reportes")
    public String reportes(Model modelo, HttpSession session) {
        
        try {
           
               //Lista de reportes
               Map<String,Map<Object,Long>> conteoComentarios = reporteServicio.obtenerConteoComentarios();
                Map<String,Map<Object,Long>> conteoPublicaciones = reporteServicio.obtenerConteoPublicaciones();
                 Map<String,Map<Object,Long>> conteoUsuarios = reporteServicio.obtenerConteoUsuarios();
                 System.out.println("Conteo Comentarios: " + conteoComentarios);
                 System.out.println("Conteo Publicaciones: " + conteoPublicaciones);
                 System.out.println("Conteo Usuarios: " + conteoUsuarios);
                modelo.addAttribute("conteoComentarios", conteoComentarios);
                modelo.addAttribute("conteoPublicaciones", conteoPublicaciones);
                modelo.addAttribute("conteoUsuarios", conteoUsuarios);
                return "admin_reportes.html";
            
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            return "error.html";
        }
    }

}
