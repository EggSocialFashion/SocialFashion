package proyecto.socialfashion.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Servicios.ReporteServicio;

@Controller
@RequestMapping("/reportar")
public class ReporteControlador {

    @Autowired
    private ReporteServicio reporteServicios;


    @PostMapping("/publicacion/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public String denunciar(@PathVariable String idPublicacion,
            @RequestBody String texto,
            @RequestParam String tipo,
            @RequestParam String tipoObjeto,
            @RequestParam String idObjeto,
            @AuthenticationPrincipal Usuario usuario, ModelMap modelo, Model model) {
            
        tipo = tipo.toUpperCase();
        tipoObjeto = tipoObjeto.toUpperCase();

        String mensajeValidacion = reporteServicios.validarDenuncia(tipo, tipoObjeto, idObjeto);
        
        if (mensajeValidacion == null || mensajeValidacion.isEmpty()) {
            try {
                boolean exito = reporteServicios.guardarReporte(texto, tipo, tipoObjeto, idObjeto, usuario);
                if (exito) {
                    modelo.addAttribute("mensaje", "Reporte guardado exitosamente");
                    return "exito";
                } else {
                    modelo.addAttribute("mensaje", "Error al guardar el reporte");
                    return "error";
                }
        } catch (IllegalArgumentException e) {
            modelo.addAttribute("mensaje", "Tipo de denuncia o tipo de objeto inválido");
            return "index.html";
        } catch (Exception e) {
            modelo.addAttribute("mensaje", "Error al guardar el reporte");
            return "index.html";
        }
        } else {
            model.addAttribute("mensaje", mensajeValidacion);
            return "reporte/publicacion"; 
        }

    }

}
