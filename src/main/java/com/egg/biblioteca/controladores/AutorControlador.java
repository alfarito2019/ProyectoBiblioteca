package com.egg.biblioteca.controladores;


import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/autor")
public class AutorControlador {
    private final AutorServicio autorServicio;

    public AutorControlador(AutorServicio autorServicio) {
        this.autorServicio = autorServicio;
    }

    @GetMapping("/registrar") // localhost:8080/autor/registrar
    public String registrar() {
        return "autor_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombreAutor, ModelMap model) {
        try {
            autorServicio.crearAutor(nombreAutor); //Llamo a mi servicio para persistir
            model.put("exito", "El autor fue registrado correctamente");
        } catch (MiException e) {

            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, e);
            model.put("error", e.getMessage());
            return "autor_form.html";
        }
            return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {


        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap modelo) {
        modelo.put("autor", autorServicio.getOne(id));
        return "autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, @RequestParam String nombre, ModelMap modelo) {
        try {
            autorServicio.modificarAutor(nombre, id);
            return "redirect:/autor/lista";
        } catch (MiException ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            modelo.put("autor", autorServicio.getOne(id)); // Add the author to the model in case of error
            return "autor_modificar.html";
        }
    }

}

