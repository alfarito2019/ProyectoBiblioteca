package com.egg.biblioteca.servicios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.EditorialRepositorio;

import org.springframework.transaction.annotation.Transactional;



@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException {
        validar(nombre);
        Editorial editorial = new Editorial(nombre);
        editorialRepositorio.save(editorial);
        System.out.println("Editorial creada");
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {
        return editorialRepositorio.findAll();

    }

    @Transactional
    public void modificarEditorial(String nombre, UUID id) throws MiException {
        validar(nombre);
        validar(id);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();

            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        }
    }
    

    private void validar(String nombre) throws MiException {
        if (nombre.isEmpty()) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
    }

    private void validar(UUID id) throws MiException {
        if (id== null || id.toString().isEmpty()) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
    }
}
