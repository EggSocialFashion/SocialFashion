package proyecto.socialfashion.Entidades;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import proyecto.socialfashion.Enumeraciones.Roles;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idUsuario;
    
    private String nombre;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Roles roles;
    private Boolean estado;
    private String email;

    @OneToOne
    private Imagen imagen;

    public Usuario() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public Roles getRoles() {
        return roles;
    }

    public Boolean getEstado() {
        return estado;
    }

    public String getEmail() {
        return email;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

   
    

}