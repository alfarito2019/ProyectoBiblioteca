package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.ImagenRepositorio;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private final ImagenServicio imagenServicio;

    @Autowired
    private final ImagenRepositorio imagenRepositorio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, ImagenServicio imagenServicio, ImagenRepositorio imagenRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.imagenServicio = imagenServicio;
        this.imagenRepositorio = imagenRepositorio;
    }

    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2)throws MiException{
        validar(nombre, email, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
            Imagen imagen = imagenServicio.guardar(archivo);
            usuario.setImagen(imagen);


        usuarioRepositorio.save(usuario);
        System.out.println("Usuario registrado con éxito");

    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {
        if (nombre.isEmpty()) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty()) {
            throw new MiException("el email no puede ser nulo o estar vacío");
        }
        if (password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);


        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }


    }

    @Transactional
    public List<Usuario> listarUsuarios() {

        return usuarioRepositorio.findAll();
    }

    public void cambiarRol(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if(respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if(usuario.getRol().equals(Rol.USER)) {
                usuario.setRol(Rol.ADMIN);
            }else if(usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
            }
        }

    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }

    public void modificar(String nombre, String id, String email,Rol rol,String imagen_id) throws MiException {
        validar(nombre);
        validar(id);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setRol(rol);
            usuario.setEmail(email);
            Imagen imagen = imagenRepositorio.getReferenceById(imagen_id);
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }
    }

    private void validar(String nombre) {
    }
}
