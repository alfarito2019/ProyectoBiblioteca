package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/libro")
public class LibroControlador {
    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")//localhost:8080/libro/registrar
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String registrar(ModelMap model) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("autores", autores);
        model.addAttribute("editoriales", editoriales);
        return "libro_form.html";
    }

    @PostMapping("/registro")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
                           @RequestParam(required = false) Integer ejemplares, @RequestParam String id_Autor,
                           @RequestParam String id_Editorial, ModelMap modelo) {
        try {
            UUID autorUUID = UUID.fromString(id_Autor);
            UUID editorialUUID = UUID.fromString(id_Editorial);
            libroServicio.crearLibro(isbn, titulo, ejemplares, autorUUID, editorialUUID);
            modelo.put("exito", "El libro fue cargado correctamente");
        } catch (IllegalArgumentException ex) {
            modelo.put("error", "ID de Autor o Editorial no válido.");
        } catch (MiException ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
        }

        // Asegúrate de volver a cargar las listas de autores y editoriales en caso de error
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html"; // volvemos a cargar el formulario.
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);
        return "libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        modelo.put("libro", libroServicio.getOne(isbn));
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "libro_modificar.html";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, @RequestParam String titulo, @RequestParam int ejemplares,
                            @RequestParam String idAutor, @RequestParam String idEditorial,  ModelMap modelo) {
        System.out.println("Aqui estamos llegando");
        try {
            UUID autorUUID = UUID.fromString(idAutor);
            UUID editorialUUID = UUID.fromString(idEditorial);
            libroServicio.modificarLibro(titulo, isbn, ejemplares, autorUUID, editorialUUID);
            List<Libro> libros = libroServicio.listarLibros();
            modelo.addAttribute("libros", libros);
            modelo.put("exito", "Libro modificado exitosamente");
            return "libro_list.html";
        } catch (Exception e) {
            modelo.put("error", "Error al modificar el libro");
            return "libro_modificar";
        }
    }


}

