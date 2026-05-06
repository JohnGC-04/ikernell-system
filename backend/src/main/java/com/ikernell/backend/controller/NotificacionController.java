package com.ikernell.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;
import com.ikernell.backend.entity.Notificacion;
import com.ikernell.backend.repository.NotificacionRepository;




@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionRepository repository;

    public NotificacionController(NotificacionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/mis-alertas")
    public List<Notificacion> getMisNotificaciones(Principal principal) {
        return repository.findByDestinatarioEmailAndLeidaFalse(principal.getName());
    }
}