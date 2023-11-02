package proyecto.socialfashion.Servicios;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import proyecto.socialfashion.Entidades.Imagen;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Categoria;
import proyecto.socialfashion.Excepciones.Excepciones;
import proyecto.socialfashion.Repositorios.PublicacionRepositorio;

@Service
public class PublicacionServicio {

    @Autowired
     PublicacionRepositorio publicacionRepositorio;
    @Autowired
     ImagenServicio imagenServicio;
     @Autowired
     private LikeServicio likeServicio;
     @Autowired
     private ComentarioServicio comentarioServicio;
    @Autowired
    private ReporteServicio reporteServicio;
    
    @Transactional()
    public void CrearPublicacion(MultipartFile archivo,String titulo ,String contenido, LocalDateTime alta, String categoria, Usuario usuario) throws Excepciones {

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setContenido(contenido);
        publicacion.setAlta(alta);

        if (categoria.equalsIgnoreCase("INDUMENTARIA")) {
            publicacion.setCategoria(Categoria.INDUMENTARIA);

        } else if (categoria.equalsIgnoreCase("MAQUILLAJE")) {
            publicacion.setCategoria(Categoria.MAQUILLAJE);

        } else if (categoria.equalsIgnoreCase("CALZADO")) {
            publicacion.setCategoria(Categoria.CALZADO);

        } else {
            publicacion.setCategoria(Categoria.MARROQUINERIA);
        }
        publicacion.setEstado(true);
        publicacion.setUsuario(usuario);

        Imagen imagen = imagenServicio.guardar(archivo,publicacion);

        publicacion.setImagen(imagen);
        
        publicacionRepositorio.save(publicacion); 
              
    }

    @Transactional(readOnly = true)
    public Publicacion getOne(String idPublicacion) {
        return publicacionRepositorio.getOne(idPublicacion);
    }
       
    
    public List<Object[]> listaPublicacionOrdenadasPorLikes() {
        List<Publicacion> listaPublicacion = publicacionRepositorio.buscarPublicacionPorFechaDeAlta(LocalDateTime.now());
        listaPublicacion = VerificarEstado(listaPublicacion);
        
        Collections.sort(listaPublicacion, new Comparator<Publicacion>() {
            @Override
            public int compare(Publicacion publicacion1, Publicacion publicacion2) {                
                int likes1 = publicacion1.getLikes().size();
                int likes2 = publicacion2.getLikes().size();                            
                int comparandoLikes = Integer.compare(likes2, likes1); 
                if (comparandoLikes != 0) {
                    return comparandoLikes;
                } else {
                    return publicacion2.getAlta().compareTo(publicacion1.getAlta());
                }                                
            }
        });
        List<Object[]> publicacionesConLikeYComentarios = new ArrayList<>();
        for (Publicacion publicacion : listaPublicacion) {
            int likes = likeServicio.totalLike(publicacion.getIdPublicacion());
            int comentarios = comentarioServicio.totalComentariosPublicacion(publicacion.getIdPublicacion());
            Object[] publicacionaux = { publicacion, likes, comentarios };
            publicacionesConLikeYComentarios.add(publicacionaux);
        }    
        if (publicacionesConLikeYComentarios.size() < 10) {
            return publicacionesConLikeYComentarios;
        } else {
            return publicacionesConLikeYComentarios.subList(0, 10);
        }
    }
    
    
    @Transactional(readOnly = true)
    public List<Publicacion> listaPublicacionOrdenadasPorFechaAlta(){        
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        listaPublicacion = publicacionRepositorio.findAll();        
        //Ordeno las publicaciones por fecha de Alta con el coleccionsSort
        Collections.sort(listaPublicacion, new Comparator<Publicacion>() {
            @Override
            public int compare(Publicacion publicacion1, Publicacion publicacion2) {

                return publicacion1.getAlta().compareTo(publicacion2.getAlta());

            }
        });
        
        //Creo una nueva lista para verificar que esten en alta  todas las publicaciones en el caso que alguna sea dada de baja por el admin
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        

        return listaVerificada;
    }
    



    @Transactional(readOnly = true)
    public List<Publicacion> listaPublicacionGuest() {
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        LocalDateTime fechaHoy = LocalDateTime.now();
        
        listaPublicacion = publicacionRepositorio.buscarPublicacionPorFechaDeAlta(fechaHoy);
        
        //Creo una nueva lista para verificar que esten en alta 
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        listaPublicacion.clear();
        //En el caso que la cantidad de publicaciones sea menor a 10
        if (listaVerificada.size() < 10){
            return listaVerificada;
        } else {
            //En el caso que la lista de publicaciones sea mayor a 10
            for (int i = 0; i < 10; i++) {
               listaPublicacion.add(listaVerificada.get(i));
            }
            return listaPublicacion;
        } 
    }
    

    @Transactional()
    public void BajaPublicacion(String idPublicacion) {

        Publicacion publicacion = publicacionRepositorio.getById(idPublicacion);
        reporteServicio.bajaPublicacionReporte(publicacion);
        publicacion.setEstado(false);

    }

    public void validar(String titulo, String contenido, LocalDateTime alta, String categoria, Usuario usuario, Imagen imagen) throws Excepciones {
       
        if (titulo.isEmpty() || titulo.equalsIgnoreCase("")) {
            throw new Excepciones("El titulo no puede estar estar vacio");
        }
        if (contenido.isEmpty() || contenido.equalsIgnoreCase("")) {
            throw new Excepciones("El contenido no puede estar estar vacio");
        }

        if (categoria.equals("") || categoria.isEmpty()) {
            throw new Excepciones("La categoria no puede ser nula o estar vacia");
        }

        if (alta == null || alta.equals(null)) {
            throw new Excepciones("La fecha de alta no puede ser nula");
        }

        if (usuario.equals(null) || usuario == null) {
            throw new Excepciones("El usuario no puede ser null");
        }

        if (imagen.equals(null) || imagen == null) {
            throw new Excepciones("La imagen no puede ser null");
        }

    }

    //Metodo para validar estado de la publicacion si no fue dada de baja
    public List<Publicacion> VerificarEstado(List<Publicacion>listaPublicacion){

        //Creo una nueva lista para verificar que esten en alta 
        List<Publicacion> listaVerificada = new ArrayList<>();
        //For para recorrer todo el arrayList y que funcione 
        for (Publicacion publicacion : listaPublicacion) {
            if (publicacion.isEstado() == true) {
                listaVerificada.add(publicacion);
            }
        }

        return listaVerificada;
    }
    
    @Transactional(readOnly = true)
    public Optional<Publicacion> buscarPublicacionPorId(String idPublicacion) {
    Optional<Publicacion> publicacion = publicacionRepositorio.findById(idPublicacion);

    return publicacion;

}

@Transactional
    public List<Publicacion> listaPublicacionPorTipo(List<String> tipos) {
        
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        //busco publicaciones cargadas
        listaPublicacion = publicacionRepositorio.findAll();
        //Creo una nueva lista de publicaciones de alta
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        List<Publicacion> listaFiltroTipo = new ArrayList<>();

        //Comparo para verificar si son iguales a los tipos que traigo
        for (Publicacion publicacion : listaVerificada) {
            for (String tipo : tipos) {
                if(publicacion.getCategoria().name().equals(tipo)){
                    listaFiltroTipo.add(publicacion);
                }
                
            }
        }
        return listaFiltroTipo;
    }


    @Transactional
    public List<Publicacion> listadoPublicacionesPorUsuario(Usuario usuario){
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        //busco publicaciones cargadas
        listaPublicacion = publicacionRepositorio.findAll();
        //Creo una nueva lista de publicaciones de alta
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        List<Publicacion> listaFiltroUsuario = new ArrayList<>();
        //Comparo para verificar si son iguales a los tipos que traigo
        for (Publicacion publicacion : listaVerificada){            
                if(publicacion.getUsuario().getIdUsuario().toString().equals(usuario.getIdUsuario())){
                    listaFiltroUsuario.add(publicacion);
                }
        }
        return listaFiltroUsuario;
    }

    
    @Transactional
    public List<Publicacion> listaPublicacionPorDiseniador(List<String> usuarios) {
        
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        //busco publicaciones cargadas
        listaPublicacion = publicacionRepositorio.findAll();
        //Creo una nueva lista de publicaciones de alta
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        List<Publicacion> listaFiltroDiseniador = new ArrayList<>();

        //Comparo para verificar si son iguales a los tipos que traigo
        for (Publicacion publicacion : listaVerificada) {
            for (String usuario : usuarios) {
                if(publicacion.getUsuario().getIdUsuario().toString().equals(usuario)){
                    listaFiltroDiseniador.add(publicacion);
                }
                
            }
        }
        return listaFiltroDiseniador;

    }

    @Transactional
    public void bajaPublicacionesDeUsuario(Usuario usuario){
        List<Publicacion> publicaciones = listadoPublicacionesPorUsuario(usuario);

        for (Publicacion publicacion : publicaciones) {
            if(publicacion.isEstado()==true){
                publicacion.setEstado(false);
                publicacionRepositorio.save(publicacion);
            }
        }
    }
    @Transactional
    public void altaPublicacionesDeUsuario(Usuario usuario){
        List<Publicacion> publicaciones = publicacionRepositorio.findAll();

        for (Publicacion publicacion : publicaciones) {
            if(publicacion.isEstado()==false&&publicacion.getUsuario().equals(usuario)){
                publicacion.setEstado(true);
                publicacionRepositorio.save(publicacion);
            }
        }
    }

}