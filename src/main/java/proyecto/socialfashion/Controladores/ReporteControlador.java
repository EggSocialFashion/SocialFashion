package proyecto.socialfashion.Controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Servicios.ReporteServicio;

@Controller
@RequestMapping("/")
public class ReporteControlador {

    @Autowired
    private ReporteServicio reporteServicios;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/reportar")
    public String reportar(@RequestParam("tipo") String tipoObjeto, @RequestParam("id") String idObjeto, Model modelo) {
        try {
            modelo.addAttribute("tipoObjeto", tipoObjeto);
            modelo.addAttribute("idObjeto", idObjeto);
            return "prueba_reportar.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "prueba_reportar.html";
        }
    }
        
        @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
        @PostMapping("/reportar")
        public String denunciar(@RequestParam String texto,
                @RequestParam String tipo,
                @RequestParam String tipoObjeto,
                @RequestParam String idObjeto,
                Model modelo, HttpSession session) {

            tipo = tipo.toUpperCase();
            tipoObjeto = tipoObjeto.toUpperCase();
            try {
                String mensajeValidacion = reporteServicios.validarDenuncia(tipo, tipoObjeto, idObjeto);
                Usuario usuario = (Usuario) session.getAttribute("usuariosession");
                if (mensajeValidacion == null || mensajeValidacion.isEmpty()) {
                    try {
                        boolean exito = reporteServicios.guardarReporte(texto, tipo, tipoObjeto, idObjeto, usuario);
                        if (exito) {
                            modelo.addAttribute("exito", "Reporte guardado exitosamente");
                        } else {
                            modelo.addAttribute("error", "Error al guardar el reporte");
                        }
                    } catch (IllegalArgumentException e) {
                        modelo.addAttribute("error", "Tipo de denuncia o tipo de objeto inválido");
                    } catch (Exception e) {
                        modelo.addAttribute("error", "Error al guardar el reporte");
                    }
                } else {
                    modelo.addAttribute("error", mensajeValidacion);

                }
                return "redirect:/publicacionesSocialFashion";
            } catch (Exception ex) {
                modelo.addAttribute("error", ex.getMessage());
                return "error.html";
            }
        }

          

}
