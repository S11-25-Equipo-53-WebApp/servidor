package com.webapp.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Este endpoint es público y no requiere token :)";
    }

    @GetMapping("/secure/hello")
    public String secureHello() {
        return "Hola! Accediste correctamente a un endpoint seguro con JWT 🎉";
    }

    // ------------------------------
    // Endpoints de prueba por roles
    // ------------------------------

    @GetMapping("/role/user")
    @PreAuthorize("hasRole('USUARIO')")
    public String userHello() {
        return "Hola USER! Solo los usuarios con rol USER pueden ver esto.";
    }

    @GetMapping("/role/admin")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String adminHello() {
        return "Hola ADMIN! Solo los usuarios con rol ADMIN pueden ver esto.";
    }
}
