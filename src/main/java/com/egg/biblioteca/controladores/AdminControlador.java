package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @RequestMapping("/dashboard")
    public String panelAdministrativo() {
        return "index.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @GetMapping("/usuario/cambiarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        try {
            usuarioServicio.cambiarRol(id);
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuario/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.getOne(id);
        modelo.addAttribute("usuario", usuario);
        return "usuario_modificar.html";
    }
    @PostMapping("/usuario/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, @RequestParam String nombre, @RequestParam String email, @RequestParam String imagen_id, @RequestParam Rol rol, ModelMap modelo) {
        Usuario usuario = usuarioServicio.getOne(id);
        modelo.addAttribute("usuario", usuario);
        try {
            usuarioServicio.modificar(nombre,id, email, rol, imagen_id);
            return "/usuarios";
        } catch (MiException ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);

            return "/usuarios";
        }
    }
}

