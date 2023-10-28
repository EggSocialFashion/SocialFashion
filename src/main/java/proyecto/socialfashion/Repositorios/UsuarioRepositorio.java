package proyecto.socialfashion.Repositorios;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.socialfashion.Entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.estado = 1")
    public List<Usuario> buscarPorNombre(@Param("nombre")String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario")
    public List<Usuario> buscarPorId(@Param("idUsuario")String idUsuario);
}