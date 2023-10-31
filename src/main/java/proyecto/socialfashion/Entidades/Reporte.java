package proyecto.socialfashion.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import proyecto.socialfashion.Enumeraciones.Estado;
import proyecto.socialfashion.Enumeraciones.Tipo;
import proyecto.socialfashion.Enumeraciones.TipoObjeto;

@Entity
public class Reporte {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idReporte;

    private String texto;
    private Estado estado;
    private Tipo tipo;
    private TipoObjeto tipoObjeto;
    private String idObjeto;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    public Reporte() {
    }

    public Reporte(String texto, Estado estado, Tipo tipo, TipoObjeto tipoObjeto, String idObjeto, Usuario idUsuario) {
        this.texto = texto;
        this.estado = estado;
        this.tipo = tipo;
        this.tipoObjeto = tipoObjeto;
        this.idObjeto = idObjeto;
        this.idUsuario = idUsuario;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public TipoObjeto getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public String getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(String idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

}