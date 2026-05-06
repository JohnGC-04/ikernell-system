package com.ikernell.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikernell.backend.entity.Notificacion;
import java.util.List;


public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByDestinatarioEmailAndLeidaFalse(String email);
}