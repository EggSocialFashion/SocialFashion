package proyecto.socialfashion.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idComentario;

    private String texto;

    private Boolean estado;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;
    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion idPublicacion;

    public Comentario() {
    }

    public Comentario(String texto, Usuario idUsuario, Publicacion idPublicacion) {
        this.texto = texto;
        this.idUsuario = idUsuario;
        this.idPublicacion = idPublicacion;
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    

    public Publicacion getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Publicacion idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}