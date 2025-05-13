package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")  // Acá es donde realizamos el mapeo
    public String index() {
        return "index.html";   // Acá es que retornamos con el método.
    }

    @GetMapping("/registrar")  // Acá es donde realizamos el mapeo
    public String registrar() {
        return "registro.html";   // Acá es que retornamos con el método.
    }

    @PostMapping("/registro")
    public String registro(@RequestParam("foto") MultipartFile foto, @RequestParam String nombre, @RequestParam String email, @RequestParam String password,
                           @RequestParam String password2, ModelMap modelo) {
        try {
            usuarioServicio.registrar(foto,nombre, email, password, password2);
            System.out.println("Usuario registrado con éxito");
            modelo.put("exito", "Usuario registrado con éxito");
            return "redirect:/index.html";
        } catch (MiException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro";
        }
    }

    @GetMapping("/login")  // Acá es donde realizamos el mapeo
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña incorrectos");
        }
        return "login.html";   // Acá es que retornamos con el método.
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "index.html";
    }


}
