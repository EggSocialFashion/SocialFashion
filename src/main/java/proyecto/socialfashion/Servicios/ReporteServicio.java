package proyecto.socialfashion.Servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Reporte;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Estado;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Enumeraciones.Tipo;
import proyecto.socialfashion.Enumeraciones.TipoObjeto;
import proyecto.socialfashion.Repositorios.ReporteRepositorio;

@Service
public class ReporteServicio {

    private UsuarioServicio usuarioServicio;
    private ComentarioServicio comentarioServicio;
    private PublicacionServicio publicacionServicio;
    private ReporteRepositorio reporteRepositorio;

    @Autowired
    public ReporteServicio(UsuarioServicio usuarioServicio,
                           ComentarioServicio comentarioServicio,
                           PublicacionServicio publicacionServicio,
                           ReporteRepositorio reporteRepositorio){
        this.usuarioServicio=usuarioServicio;
        this.comentarioServicio=comentarioServicio;
        this.publicacionServicio=publicacionServicio;
        this.reporteRepositorio= reporteRepositorio;
                                    
    }


    @Transactional
    public boolean guardarReporte(String texto, String tipo, String tipoObjeto, String idObjeto, Usuario usuario) {
        Tipo tipoEnum = Tipo.valueOf(tipo);
        TipoObjeto tipoObjetoEnum = TipoObjeto.valueOf(tipoObjeto);
    
        Reporte reporte = new Reporte(texto, Estado.PENDIENTE, tipoEnum, tipoObjetoEnum, idObjeto, usuario);
        Reporte reporteGuardado = reporteRepositorio.save(reporte);
    
        return reporteGuardado != null;
    }
    

    //VALIDACION PARA EL CONTROLADOR
    public String validarDenuncia(String tipo, String tipoObjeto, String idObjeto) {
        
        if (!Tipo.isValid(tipo)) {
            return "Tipo de denuncia inválido";
        }

        if (!TipoObjeto.isValid(tipoObjeto)) {
            return "Tipo de objeto de denuncia inválido";
        }

        if (TipoObjeto.COMENTARIO.toString().equals(tipoObjeto)) {
            Optional<Comentario> respuestaC = comentarioServicio.buscarComentarioPorId(idObjeto);
            if (!respuestaC.isPresent()) {
                return "No se encontró comentario";
            }
        } else if (TipoObjeto.PUBLICACION.toString().equals(tipoObjeto)) {
            Optional<Publicacion> respuestaP = publicacionServicio.buscarPublicacionPorId(idObjeto);
            if (!respuestaP.isPresent()) {
                return "No se encontró Publicación";
            }
        } else if (TipoObjeto.USUARIO.toString().equals(tipoObjeto)) {
            Optional<Usuario> respuestaU = usuarioServicio.buscarUsuarioOptionalId(idObjeto);
            if (!respuestaU.isPresent()) {
                return "No se encontró Usuario";
            }
        }

        return null;
    }



    @Transactional
    public void desestimarReporte(Reporte reporte) {
        Reporte reporteAux = reporte;
        reporteAux.setEstado(Estado.DESESTIMADO);
        reporteRepositorio.save(reporteAux);
    }
    @Transactional
    public void aceptarReporte(Reporte reporte) {
        Reporte reporteAux = reporte;
        reporteAux.setEstado(Estado.ACEPTADO);
        reporteRepositorio.save(reporteAux);
    }

    @Transactional
    public Optional<Reporte> buscarReportePorId(String id) {

        return reporteRepositorio.findById(id);
    }

    @Transactional
    public List<Reporte> obtenerUsuariosReportadosPendientes() {
        List<Reporte> aux = reporteRepositorio.findAll();
        List<Reporte> reporte = aux.stream()
                .filter(reportes -> reportes.getEstado() == Estado.PENDIENTE && reportes.getTipoObjeto() == TipoObjeto.USUARIO)
                .collect(Collectors.toList());
        System.out.print(reporte.size());
        return reporte;


    }

    @Transactional
    public List<Reporte> obtenerComentariosReportadosPendientes(){
        List<Reporte> reportes = reporteRepositorio.findAll();
        return reportes.stream()
        .filter(reporte -> reporte.getEstado() == Estado.PENDIENTE && reporte.getTipoObjeto() == TipoObjeto.COMENTARIO)
        .collect(Collectors.toList());
    }
    
    
    @Transactional
    public List<Reporte> obtenerPublicacionesReportadasPendientes() {
        List<Reporte> reportes = reporteRepositorio.findAll();
    
        return reportes.stream()
            .filter(reporte -> reporte.getEstado() == Estado.PENDIENTE && reporte.getTipoObjeto() == TipoObjeto.PUBLICACION)
            .collect(Collectors.toList());
    }

    @Transactional
    public String procesarReporte(String estado, String idObjeto, String idReporte, Usuario usuario, Model modelo, Object objeto) {
        if (usuario.getRoles() != Roles.ADMIN) {
            return "Usuario no permitido";

        }

        Optional<Reporte> respuestaR = buscarReportePorId(idReporte);
        Reporte reporte = new Reporte();
        if (respuestaR.isPresent()) {
            reporte = respuestaR.get();
            estado = estado.toUpperCase();
            if (estado.equals(Estado.ACEPTADO.toString())) {
                if (esEstadoActivo(objeto)) {
                    desactivarObjeto(objeto);
                    aceptarReporte(reporte);
                    return objeto.getClass().getSimpleName() + " dado de baja";
                } else {
                    return objeto.getClass().getSimpleName() + " ya estaba dado de baja";
                }
            } else if (estado.equals(Estado.DESESTIMADO.toString())) {
                desestimarReporte(reporte);
            } else {
                return "Opción no permitida";
            }
        } else {
            return "Reporte inexistente";
        }
        return null;
    }

    private boolean esEstadoActivo(Object objeto) {
        if (objeto instanceof Usuario) {
            return ((Usuario) objeto).getEstado();
        } else if (objeto instanceof Comentario) {
            return ((Comentario) objeto).getEstado();
        } else if (objeto instanceof Publicacion) {
            return ((Publicacion) objeto).isEstado();
        }
        return false; 
    }

    private void desactivarObjeto(Object objeto) {
        if (objeto instanceof Usuario) {
            usuarioServicio.cambiarEstado(((Usuario) objeto).getIdUsuario());
        } else if (objeto instanceof Comentario) {
            comentarioServicio.cambiarEstado(((Comentario) objeto).getIdComentario());
        } else if (objeto instanceof Publicacion) {
            publicacionServicio.BajaPublicacion(((Publicacion) objeto).getIdPublicacion());
        }
    }
}



