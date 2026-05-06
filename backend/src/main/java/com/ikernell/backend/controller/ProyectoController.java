package com.ikernell.backend.controller;

import com.ikernell.backend.dto.ProyectoDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.service.ProyectoService;
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
    public ResponseEntity<ProyectoDTO> crear(@RequestBody Proyecto proyecto) {
        return new ResponseEntity<>(proyectoService.guardar(proyecto), HttpStatus.CREATED);
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
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        double costoActual = proyecto.getEtapas().stream()
                .flatMap(etapa -> etapa.getActividades().stream())
                .mapToDouble(Actividad::getCostoEstimado)
                .sum();

        Map<String, Object> balance = new HashMap<>();
        balance.put("nombreProyecto", proyecto.getNombre());
        balance.put("presupuestoTotal", proyecto.getPresupuesto());
        balance.put("costoConsumido", costoActual);
        balance.put("saldoDisponible", proyecto.getPresupuesto() - costoActual);

        return ResponseEntity.ok(balance);
    }
}