package com.egg.biblioteca.servicios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepositorio;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException {
        validar(nombre);
        Autor autor = new Autor(nombre);
        autorRepositorio.save(autor);
        System.out.println("Autor creado");
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {
        return autorRepositorio.findAll();
    }

    @Transactional
    public void modificarAutor(String nombre, UUID id) throws MiException {
        validar(nombre);
        validar(id);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();

            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        }
    }

    @Transactional(readOnly = true)
    public Autor  getOne(UUID id){
        return autorRepositorio.getReferenceById (id);
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
