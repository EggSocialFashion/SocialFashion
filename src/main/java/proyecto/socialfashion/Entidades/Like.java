package proyecto.socialfashion.Entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_likes")
    private String idLike;
    
    @ManyToOne
    @JoinColumn(name = "id_Publicacion")
    private Publicacion publicacion;

    @OneToOne
    @JoinColumn(name = "id_Usuario")
    private Usuario usuario;
    private Boolean estado;
    public Like() {
        
    }
    public Like(String idLike, Publicacion publicacion, Usuario usuario, Boolean estado) {
        this.idLike = idLike;
        this.publicacion = publicacion;
        this.usuario = usuario;
        this.estado = estado;
    }        
    public String getIdLike() {
        return idLike;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setLike(String idLike) {
        this.idLike = idLike;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    


    
}