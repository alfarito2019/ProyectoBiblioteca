package com.egg.biblioteca.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.egg.biblioteca.excepciones.MiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;

@Service
public class LibroServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, int ejemplares, UUID idAutor, UUID idEditorial) throws MiException {
        Libro libro = new Libro();
        validar(titulo);
        validar(ejemplares);
        validar(idAutor);
        validar(idEditorial);
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        Optional<Autor> autor = autorRepositorio.findById(idAutor);
        Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);
        if (!autor.isPresent()) {
            throw new RuntimeException("No se encontro el autor");
        }
        if (!editorial.isPresent()) {
            throw new RuntimeException("No se encontro la editorial");
        }
        libro.setAutor(autor.get());
        libro.setEditorial(editorial.get());
        libroRepositorio.save(libro);
        System.out.println("Libro creado");
    }


    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();

    }


    @Transactional
    public void modificarLibro(String titulo, Long isbn, int ejemplares, UUID idAutor, UUID idEditorial) {
        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            Optional<Autor> autor = autorRepositorio.findById(idAutor);
            Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);
            libro.setAutor(autor.get());
            libro.setEditorial(editorial.get());

            libroRepositorio.save(libro);
        }
    }

    private void validar(String nombre) throws MiException {
        if (nombre.isEmpty()) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
    }

    private void validar(UUID id) throws MiException {
        if (id == null || id.toString().isEmpty()) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
    }

    private void validar(int ejemplares) {
        if (ejemplares < 0) {
            throw new RuntimeException("Esta cantidad no puede ser negativa");
        }
    }

    public Libro getOne(Long id) {
        return libroRepositorio.getReferenceById(id);
    }
}
