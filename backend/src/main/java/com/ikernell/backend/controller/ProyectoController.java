package com.ikernell.backend.controller;

import com.ikernell.backend.dto.ProyectoDTO;
import com.ikernell.backend.entity.AuditoriaPresupuesto;
import com.ikernell.backend.service.ProyectoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @PreAuthorize("hasAuthority('COORDINADOR')")
    @PostMapping
    public ResponseEntity<ProyectoDTO> crearProyecto(@Valid @RequestBody ProyectoDTO dto) {
        return new ResponseEntity<>(proyectoService.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('LIDER', 'COORDINADOR')") // Cambiado de hasAnyRole
    public ResponseEntity<ProyectoDTO> actualizarProyecto(
            @PathVariable Long id,
            @Valid @RequestBody ProyectoDTO dto) {
        String emailAutor = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        return ResponseEntity.ok(proyectoService.actualizar(id, dto, emailAutor));
    }

    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> listar() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @GetMapping("/{id}/costo-total")
@PreAuthorize("hasAuthority('LIDER')") // Cambiado de hasRole
    public ResponseEntity<Double> obtenerCostoTotal(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.calcularCostoTotal(id));
    }

    @PreAuthorize("hasAnyAuthority('LIDER', 'COORDINADOR')")
    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, Object>> obtenerBalanceProyecto(@PathVariable Long id) {
        // LLAMAMOS AL SERVICE, NO AL REPOSITORY
        Map<String, Object> balance = proyectoService.obtenerBalanceCuentas(id);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/{id}/historial-financiero")
    @PreAuthorize("hasAuthority('COORDINADOR')")
    public ResponseEntity<List<AuditoriaPresupuesto>> obtenerHistorial(@PathVariable Long id) {
        // Delegamos la búsqueda al Service para mantener limpio el Controller
        return ResponseEntity.ok(proyectoService.obtenerHistorialFinanciero(id));
    }
}