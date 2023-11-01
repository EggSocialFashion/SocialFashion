package proyecto.socialfashion.Servicios;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.socialfashion.Entidades.Comentario;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Reporte;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Estado;
import proyecto.socialfashion.Enumeraciones.Tipo;
import proyecto.socialfashion.Enumeraciones.TipoObjeto;
import proyecto.socialfashion.Repositorios.ReporteRepositorio;

@Service
public class ReporteServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ComentarioServicio comentarioServicio;
    @Autowired
    private PublicacionServicio publicacionServicio;
    @Autowired
    private ReporteRepositorio reporteRepositorio;


    // guardar una denuncia
    @Transactional
    public boolean guardarReporte(String texto, String tipo, String tipoObjeto, String idObjeto, Usuario usuario) {
        // Valida que el tipo sea correcto(SPAM, CONTENIDO_OFENSIVO,
        // INCUMPLIMIENTO_DE_REGLAS; )
        Tipo tipoEnum = Tipo.valueOf(tipo);
        // valida que el tipo de objeto USUARIO, PUBLICACION O COMENTARIO
        TipoObjeto tipoObjetoEnum = TipoObjeto.valueOf(tipoObjeto);
        // Crea un nuevo reporte con los datos pasados por parametro
        Reporte reporte = new Reporte(texto, Estado.PENDIENTE, tipoEnum, tipoObjetoEnum, idObjeto, usuario);
        // guarda el reporte
        Reporte reporteGuardado = reporteRepositorio.save(reporte);
        // retorna que si el objeto se guardo o no
        return reporteGuardado != null;
    }

    // DESESTIMAR REPORTE
    @Transactional
    public boolean desestimarReporte(Reporte reporte) {
        boolean desestimarReporte = true;
        Optional<Reporte> reporteAux = reporteRepositorio.findById(reporte.getIdReporte());
        if(!reporteAux.isPresent()){
            desestimarReporte=false;
        }else{
            Reporte reporteF = reporteAux.get();
            reporteF.setEstado(Estado.DESESTIMADO);
            reporteRepositorio.save(reporteF);
        }

       
       return desestimarReporte;
    }

    // ACEPTAR REPORTE
    @Transactional
    public boolean aceptarReporte(Reporte reporte) {
        boolean aceptarReporte = true;
        // controlo que el reporte exista
        Optional<Reporte> auxR = reporteRepositorio.findById(reporte.getIdReporte());
        if (auxR.isPresent()) {
            // valido que tipo de objeto es
            //Estado.ACEPTADO.name().equals(estado)
            if (TipoObjeto.COMENTARIO.name().equals(reporte.getTipoObjeto().toString())) {
                // verifico que si es un comentario exista
                Optional<Comentario> respuestaC = comentarioServicio.buscarComentarioPorId(reporte.getIdObjeto());
                if (respuestaC.isPresent()) {
                    Comentario comentario = respuestaC.get();
                    comentarioServicio.borrarComentario(comentario.getIdComentario());
                } else {
                    aceptarReporte = false;
                }
            } else if (TipoObjeto.PUBLICACION.name().equals(reporte.getTipoObjeto().toString())) {
                // verifico que si es una publicacion exista
                Optional<Publicacion> respuestaP = publicacionServicio.buscarPublicacionPorId(reporte.getIdObjeto());
                if (respuestaP.isPresent()) {
                    Publicacion publicacion = respuestaP.get();
                    publicacionServicio.BajaPublicacion(publicacion.getIdPublicacion());
                } else {
                    aceptarReporte = false;
                }
            } else if (TipoObjeto.USUARIO.name().equals(reporte.getTipoObjeto().toString())) {
                // verifico que si es un usuario exista
                Optional<Usuario> respuestaU = usuarioServicio.buscarUsuarioOptionalId(reporte.getIdObjeto());
                if (respuestaU.isPresent()) {
                    Usuario usuario = respuestaU.get();
                    usuarioServicio.cambiarEstado(usuario.getIdUsuario());
                } else {
                    aceptarReporte = false;
                }
            }else{
                aceptarReporte=false;
            }

        } else {
            aceptarReporte = false;
        }
        if (aceptarReporte) {
            // desestimo reporte solo si paso todos los controles
            Reporte reporteAux = reporte;
            reporteAux.setEstado(Estado.ACEPTADO);
            reporteRepositorio.save(reporteAux);
        }
        // devuelvo false si algo fallo y true si salió todo bien.
        return aceptarReporte;
    }

    // buscar reporte por id
    @Transactional
    public Optional<Reporte> buscarReportePorId(String id) {
        // retorna el reporte como optional
        return reporteRepositorio.findById(id);
    }

    // lista de usuarios retortados
    @Transactional
    public List<Reporte> obtenerUsuariosReportadosPendientes() {
        // busca todos los reportes
        List<Reporte> aux = reporteRepositorio.findAll();
        // mapea verificando que tenga estado pendiente y sea de tipo objeto=USUARIO
        List<Reporte> reporte = aux.stream()
                .filter(reportes -> reportes.getEstado() == Estado.PENDIENTE
                        && reportes.getTipoObjeto() == TipoObjeto.USUARIO)
                .collect(Collectors.toList());
        // Devuelve el reporte
        return reporte;

    }


    // lista de comentarios retortados
    @Transactional
    public List<Reporte> obtenerComentariosReportadosPendientes() {
        // genera una lista de reportes
        List<Reporte> reportes = reporteRepositorio.findAll();
        // mapea que tenga estado pendiente y que el tipo de objeto sea un comentario
        return reportes.stream()
                .filter(reporte -> reporte.getEstado() == Estado.PENDIENTE
                        && reporte.getTipoObjeto() == TipoObjeto.COMENTARIO)
                .collect(Collectors.toList());
    }

    // lista de comentarios retortados
    @Transactional
    public List<Reporte> obtenerPublicacionesReportadasPendientes() {
        // genera una lista de reportes

        List<Reporte> reportes = reporteRepositorio.findAll();
        // mapea que tenga estado pendiente y que el tipo de objeto sea una publicacion
        return reportes.stream()
                .filter(reporte -> reporte.getEstado() == Estado.PENDIENTE
                        && reporte.getTipoObjeto() == TipoObjeto.PUBLICACION)
                .collect(Collectors.toList());
    }
    
    // metodo de validación de si el reporte existe cuando se lo busca
    public String validarDenuncia(String tipo, String tipoObjeto, String idObjeto) {
        // Valida que el tipo sea correcto(SPAM, CONTENIDO_OFENSIVO,
        // INCUMPLIMIENTO_DE_REGLAS; )
        if (!Tipo.isValid(tipo)) {
            return "Tipo de denuncia inválido";
        }
        // valida que el tipo de objeto USUARIO, PUBLICACION O COMENTARIO

        if (!TipoObjeto.isValid(tipoObjeto)) {
            return "Tipo de objeto de denuncia inválido";
        }
        // valida or tipo si existe
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
        // si existe devuelve null
        return null;
    }

    // lista de comentarios retortados sin controlar si esta pendiente
    @Transactional
    public Map<String, Map<Object, Long>> obtenerConteoComentarios() {
        List<Reporte> reportes = reporteRepositorio.findAll();
        
        // Filtrar los reportes que tengan el tipo de objeto COMENTARIO
        List<Reporte> comentarios = reportes.stream()
                .filter(reporte -> reporte.getTipoObjeto() == TipoObjeto.COMENTARIO)
                .collect(Collectors.toList());
        
        // Agrupar los comentarios por idObjeto y contar los estados
        Map<String, Map<Object, Long>> conteoPorIdObjeto = comentarios.stream()
                .collect(Collectors.groupingBy(
                        Reporte::getIdObjeto,
                        Collectors.groupingBy(
                                reporte -> reporte.getEstado().toString(), // Convertir el estado a String
                                Collectors.counting()
                        )
                ));

        
        return conteoPorIdObjeto;
    }

    // lista de comentarios retortados pendientes o no
    @Transactional
    public Map<String,Map<Object,Long>> obtenerConteoPublicaciones() {
        // genera una lista de reportes

        List<Reporte> reportes = reporteRepositorio.findAll();
        // mapea que tenga el tipo de objeto sea una publicacion
        List<Reporte> publicaciones = reportes.stream()
                .filter(reporte ->  reporte.getTipoObjeto() == TipoObjeto.PUBLICACION)
                .collect(Collectors.toList());
        Map<String,Map<Object,Long>> conteoPorIdObjeto = publicaciones.stream()
                .collect(Collectors.groupingBy(Reporte::getIdObjeto,
                Collectors.groupingBy(
                            reporte ->reporte.getEstado().toString(),
                            Collectors.counting()
                )
                ));
        return conteoPorIdObjeto;
    }
    
// lista de usuarios retortados pendientes o no
    @Transactional
    public Map<String,Map<Object,Long>> obtenerConteoUsuarios() {
        // genera una lista de reportes

        List<Reporte> reportes = reporteRepositorio.findAll();
        // mapea que tenga el tipo de objeto sea una publicacion
        List<Reporte> usuarios = reportes.stream()
                .filter(reporte ->  reporte.getTipoObjeto() == TipoObjeto.USUARIO)
                .collect(Collectors.toList());

        Map<String,Map<Object,Long>> conteoPorIdObjeto = usuarios.stream()
                .collect(Collectors.groupingBy(Reporte::getIdObjeto,
                Collectors.groupingBy(
                            reporte ->reporte.getEstado().toString(),
                            Collectors.counting()
                )
                ));
        return conteoPorIdObjeto;

    }
    
}
