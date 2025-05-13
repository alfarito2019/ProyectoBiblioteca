package com.egg.biblioteca;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
public class UsuarioRepositorioTest {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    public void testSaveUsuarioWithImage() throws Exception {
        // Simulate an image file
        InputStream inputStream = getClass().getResourceAsStream("/test-image.jpg");
        byte[] imageBytes = IOUtils.toByteArray(inputStream);
        MockMultipartFile mockImage = new MockMultipartFile("foto", "test-image.jpg", "image/jpeg", imageBytes);

        // Create a new image entity
        Imagen imagen = new Imagen();
        imagen.setMime(mockImage.getContentType());
        imagen.setNombre(mockImage.getOriginalFilename());
        imagen.setContenido(mockImage.getBytes());

        // Create a new user
        Usuario usuario = new Usuario();
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");
        usuario.setRol(Rol.USER);
        usuario.setImagen(imagen);

        // Save the user
        Usuario savedUsuario = usuarioRepositorio.save(usuario);

        // Assert that the user and image were saved
        assertNotNull(savedUsuario);
        assertNotNull(savedUsuario.getId());
        assertNotNull(savedUsuario.getImagen());
        assertNotNull(savedUsuario.getImagen().getContenido());
    }
}