package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.servicios.ImagenServicio;
import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ImagenServicio imagenServicio;
    // Obtener imagen de perfil de usuario
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {
        Usuario usuario = usuarioServicio.getOne(id);
        byte[] imagen = usuario.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        System.out.println("Imagen de perfil obtenida con éxito");
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    // Subir imagen de perfil para un usuario
    @PostMapping("/perfil/{id}")
    public ResponseEntity<String> actualizarImagenPerfil(@PathVariable String id,
                                                         @RequestParam("archivo") MultipartFile archivo) {
        try {
// Llamada al servicio para guardar la imagen
            imagenServicio.actualizar(archivo, id);
            return new ResponseEntity<>("Imagen actualizada exitosamente",

                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar la imagen",

                    HttpStatus.BAD_REQUEST);
        }
    }}
