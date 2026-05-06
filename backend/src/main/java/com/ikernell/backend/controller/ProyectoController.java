package com.ikernell.backend.controller;

import com.ikernell.backend.dto.ProyectoDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.service.ProyectoService;

import jakarta.validation.Valid;

import com.ikernell.backend.repository.ProyectoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final ProyectoRepository proyectoRepository;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
        this.proyectoRepository = null;
    }

    @PostMapping
    @PreAuthorize("hasRole('COORDINADOR')") // Solo el coordinador crea proyectos
    public ResponseEntity<ProyectoDTO> crearProyecto(@Valid @RequestBody ProyectoDTO dto) {
        return new ResponseEntity<>(proyectoService.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIDER', 'COORDINADOR')")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(
            @PathVariable Long id,
            @Valid @RequestBody ProyectoDTO dto) {
        return ResponseEntity.ok(proyectoService.actualizar(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> listar() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @PreAuthorize("hasRole('LIDER')")
    @GetMapping("/{id}/costo-total")
    public ResponseEntity<Double> obtenerCostoTotal(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.calcularCostoTotal(id));
    }

    @PreAuthorize("hasAnyRole('LIDER', 'COORDINADOR')")
    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, Object>> obtenerBalanceProyecto(@PathVariable Long id) {
        // LLAMAMOS AL SERVICE, NO AL REPOSITORY
        Map<String, Object> balance = proyectoService.obtenerBalanceCuentas(id);
        return ResponseEntity.ok(balance);
    }
}