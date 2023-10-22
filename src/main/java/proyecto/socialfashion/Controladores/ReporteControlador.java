package proyecto.socialfashion.Controladores;

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

import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Servicios.ReporteServicio;

@Controller
@RequestMapping("/")
public class ReporteControlador {

    @Autowired
    private ReporteServicio reporteServicios;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/reportar")
    public String reportar(Model modelo) {
        try {
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
            ModelMap modelo, Model model, HttpSession session) {

        tipo = tipo.toUpperCase();
        tipoObjeto = tipoObjeto.toUpperCase();

        String mensajeValidacion = reporteServicios.validarDenuncia(tipo, tipoObjeto, idObjeto);
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (mensajeValidacion == null || mensajeValidacion.isEmpty()) {
            try {
                boolean exito = reporteServicios.guardarReporte(texto, tipo, tipoObjeto, idObjeto, usuario);
                if (exito) {
                    modelo.addAttribute("mensaje", "Reporte guardado exitosamente");
                    return "prueba_reportar.html";
                } else {
                    modelo.addAttribute("mensaje", "Error al guardar el reporte");
                    return "error";
                }
            } catch (IllegalArgumentException e) {
                modelo.addAttribute("mensaje", "Tipo de denuncia o tipo de objeto inválido");
                return "prueba_reportar.html";
            } catch (Exception e) {
                modelo.addAttribute("mensaje", "Error al guardar el reporte");
                return "prueba_reportar.html";
            }
        } else {
            model.addAttribute("mensaje", mensajeValidacion);
            return "prueba_reportar.html";
        }

    }

}

