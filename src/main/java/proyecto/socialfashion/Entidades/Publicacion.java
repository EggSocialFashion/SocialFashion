package proyecto.socialfashion.Entidades;


import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;
import proyecto.socialfashion.Enumeraciones.Categoria;

@Entity
public class Publicacion {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idPublicacion;
    
    @Basic
    private String titulo;
    
    @Basic
    private String contenido;
    
    
    private LocalDateTime alta;
     
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
   
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;
   
    
    @OneToOne
    @JoinColumn(name = "id_Imagen")
    private Imagen imagen;

    public Publicacion() {

    }

    public Publicacion(String idPublicacion, String titulo, String contenido, LocalDateTime alta, Categoria categoria, boolean estado, Usuario usuario, List<Like> likes, Imagen imagen) {
        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.alta = alta;
        this.categoria = categoria;
        this.estado = estado;
        this.usuario = usuario;
        /*this.likes = likes;*/
        this.imagen = imagen;
    }

 


    public String getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getAlta() {
        return alta;
    }

    public void setAlta(LocalDateTime alta) {
        this.alta = alta;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }


    
    
    
    
    
}