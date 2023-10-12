package proyecto.socialfashion.Servicios;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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

    @Transactional
    public void CrearPublicacion(MultipartFile archivo,String titulo ,String contenido, Date alta, String categoria, Usuario usuario) throws Excepciones {

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

        publicacion.setUsuario(usuario);

        Imagen imagen = imagenServicio.guardar(archivo,publicacion);

        publicacion.setImagen(imagen);
        
        publicacionRepositorio.save(publicacion); 
              
    }

    @Transactional()
    public Publicacion getOne(String idPublicacion) {
        return publicacionRepositorio.getOne(idPublicacion);
    }
    
    //Trabajar en este servicio
/*
    @Transactional()
    public List<Publicacion> listaPublicacionOrdenadasPorLikes() {

        List<Publicacion> listaPublicacion = new ArrayList<>();
        listaPublicacion = publicacionRepositorio.findAll();

        // Se crea una collection sort y se ordena por likes de noticia
        Collections.sort(listaPublicacion, new Comparator<Publicacion>() {
            @Override
            public int compare(Publicacion publicacion1, Publicacion publicacion2) {
                for (Publicacion likes1 : publicacion1) {
                    
                }
                
                for (Publicacion likes2 : publicacion2) {
                    
                }
                List<int> likes1 = publicacion1.getLikes();
                int likes2 = publicacion2.getLikes();
                return Integer.compare(likes2, likes1);

            }
        });
        
       //Creo una nueva lista para verificar que esten en alta 
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        
        return listaVerificada;
    }
*/
    @Transactional()
    public List<Publicacion> listaPublicacionOrdenadasPorFechaAlta() {
        
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        listaPublicacion = publicacionRepositorio.findAll();

        // Se crea una collection sort y se ordena por likes de noticia
        Collections.sort(listaPublicacion, new Comparator<Publicacion>() {
            @Override
            public int compare(Publicacion publicacion1, Publicacion publicacion2) {

                return publicacion1.getAlta().compareTo(publicacion2.getAlta());

            }
        });
        
        //Creo una nueva lista para verificar que esten en alta 
        List<Publicacion> listaVerificada = VerificarEstado(listaPublicacion);
        

        return listaVerificada;
    }
    
    @Transactional()
    public List<Publicacion> listaPublicacionGuest() {
        
        //Creo lista para guardar las publicaciones
        List<Publicacion> listaPublicacion = new ArrayList<>();
        Date fechaHoy = new Date(); 
        listaPublicacion = publicacionRepositorio.buscarPrimeras10PorFechaDeAlta(fechaHoy);
        
        List<Publicacion> GuardarPrimeras10 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GuardarPrimeras10.add(listaPublicacion.get(i));
        }
        //Creo una nueva lista para verificar que esten en alta 
        List<Publicacion> listaVerificada = VerificarEstado(GuardarPrimeras10);
        return listaVerificada;
    }
    

    @Transactional()
    public void BajaPublicacion(String idPublicacion) {

        Publicacion publicacion = publicacionRepositorio.getById(idPublicacion);
        publicacion.setEstado(false);

    }

    public void validar(String titulo, String contenido, Date alta, String categoria, Usuario usuario, Imagen imagen) throws Excepciones {
       
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
    
    
    
}