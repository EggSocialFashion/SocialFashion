
package proyecto.socialfashion.Entidades;


import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    private String Contenido;
    
    @Temporal(TemporalType.DATE)
    private Date alta;
     
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
   
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private Usuario usuario;
    
    /*
    @OneToMany
    private List<Comentario> comentarios;
    */
    
    @OneToOne
    @JoinColumn(name = "id_Imagen")
    private Imagen imagen;

    public Publicacion() {

    }

    public Publicacion(String idPublicacion, String titulo, String Contenido, Date alta, Categoria categoria, boolean estado, Usuario usuario, Imagen imagen) {
        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.Contenido = Contenido;
        this.alta = alta;
        this.categoria = categoria;
        this.estado = estado;
        this.usuario = usuario;
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
        return Contenido;
    }

    public void setContenido(String Contenido) {
        this.Contenido = Contenido;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
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

/*
    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
*/

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