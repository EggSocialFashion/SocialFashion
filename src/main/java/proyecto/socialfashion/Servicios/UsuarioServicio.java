
package proyecto.socialfashion.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Excepciones.Excepciones;
import proyecto.socialfashion.Repositorios.UsuarioRepositorio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String nombre, String email, String password, String password2) throws Excepciones {

        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setEmail(email);

        usuario.setRoles(Roles.USER);
        usuario.setEstado(true);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void actualizar(String idUsuario, String nombre, String email, String password, String password2, Roles rol)
            throws Excepciones {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            if (usuario.getRoles().equals(Roles.USER)) {
                usuario.setRoles(Roles.USER);
            } else if (usuario.getRoles().equals(Roles.ADMIN)) {
                usuario.setRoles(Roles.ADMIN);
            }

            usuario.setEstado(true);

            usuarioRepositorio.save(usuario);
        }
    }

    public Usuario getOne(String idUsuario) {
        return usuarioRepositorio.getOne(idUsuario);
    }

    @Transactional
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;

    }

    @Transactional
    public List<Usuario> buscarUsuarioPorNombre(String nombre) throws Excepciones {

        List<Usuario> usuario = usuarioRepositorio.buscarPorNombre(nombre);
        return usuario;

    }

    @Transactional
    public void cambiarRol(String idUsuario) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRoles().equals(Roles.USER)) {

                usuario.setRoles(Roles.ADMIN);

            } else if (usuario.getRoles().equals(Roles.ADMIN)) {

                usuario.setRoles(Roles.USER);
            }
        }

    }

    @Transactional
    public void cambiarEstado(String idUsuario) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getEstado() == true) {

                usuario.setEstado(false);

            } else if (usuario.getEstado() == false) {

                usuario.setEstado(true);
            }
        }

    }
    
     @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRoles().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    public void validar(String nombre, String email, String password, String password2) throws Excepciones {

        if (nombre.isEmpty() || nombre == null) {
            throw new Excepciones("El nombre no puede estar vacío");
        }

        if (email.isEmpty() || email == null) {
            throw new Excepciones("El email no puede estar vacío");
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new Excepciones("El password no puede estar vacío y debe tener más de 5 dígitos.");
        }

        if (!password.equals(password2)) {
            throw new Excepciones("Las contraseñas ingresadas deben ser iguales.");
        }
    }

    @Transactional
    public Optional<Usuario> buscarUsuarioOptionalId(String id) {

        return usuarioRepositorio.findById(id);
    }

}