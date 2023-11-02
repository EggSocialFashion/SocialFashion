package proyecto.socialfashion.Servicios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import proyecto.socialfashion.Entidades.Imagen;
import proyecto.socialfashion.Entidades.Publicacion;
import proyecto.socialfashion.Entidades.Usuario;
import proyecto.socialfashion.Enumeraciones.Roles;
import proyecto.socialfashion.Excepciones.Excepciones;
import proyecto.socialfashion.Repositorios.UsuarioRepositorio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private ReporteServicio reporteServicio;
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2)
            throws Excepciones {

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setEmail(email);

        usuario.setRoles(Roles.USER);
        usuario.setEstado(true);
        validar(nombre, email, password, password2, usuario);

        Imagen imagen = imagenServicio.guardarFotoPerfil(archivo, usuario);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password,
            String password2, Roles rol)
            throws Excepciones {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            validarActualizacion(nombre, email, password, password2);
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            if (usuario.getRoles().equals(Roles.USER)) {
                usuario.setRoles(Roles.USER);
            } else if (usuario.getRoles().equals(Roles.ADMIN)) {
                usuario.setRoles(Roles.ADMIN);
            }

            usuario.setEstado(true);

            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen, usuario);

            usuario.setImagen(imagen);

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

    public Optional<Usuario> buscarUsuarioPorId(String idUsuario) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(idUsuario);

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
                reporteServicio.bajaUsuarioReporte(usuario);
                publicacionServicio.bajaPublicacionesDeUsuario(usuario);

            } else if (usuario.getEstado() == false) {

                usuario.setEstado(true);
                publicacionServicio.altaPublicacionesDeUsuario(usuario);
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

    public void validar(String nombre, String email, String password, String password2, Usuario usuario)
            throws Excepciones {

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

        List<Usuario> listaUsuario = listarUsuarios();

        for (Usuario listaUsuarios : listaUsuario) {
            if (listaUsuarios.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                throw new Excepciones("Ya existe un usuario registrado con ese Email");
            }
        }

    }

    public void validarActualizacion(String nombre, String email, String password, String password2)
            throws Excepciones {

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

    @Transactional
    public List<Usuario> diseniadores() {
        // busco las publicaciones dadas de alta
        List<Publicacion> auxPublicacion = publicacionServicio.listaPublicacionOrdenadasPorFechaAlta();

        // Usar un conjunto para almacenar diseñadores únicos
        Set<Usuario> disenadoresUnicos = new HashSet<>();

        for (Publicacion publicacion : auxPublicacion) {
            disenadoresUnicos.add(publicacion.getUsuario());
        }
        // Convertir el conjunto de diseñadores únicos de nuevo a una lista
        List<Usuario> diseniadores = new ArrayList<>(disenadoresUnicos);
        return diseniadores;
    }

}