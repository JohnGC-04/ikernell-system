package com.ikernell.backend.controller;

import com.ikernell.backend.dto.ActividadDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.service.ActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> crear(@RequestBody Actividad actividad) {
        return new ResponseEntity<>(actividadService.guardar(actividad), HttpStatus.CREATED);
    }

    // Endpoint para ver actividades de una etapa específica
    @GetMapping("/etapa/{idEtapa}")
    public ResponseEntity<List<ActividadDTO>> listarPorEtapa(@PathVariable Long idEtapa) {
        // Nota: Tendrás que agregar este método 'listarPorEtapa' en tu ActividadService
        // similar a como hicimos en EtapaService
        return ResponseEntity.ok(actividadService.listarPorEtapa(idEtapa));
    }

    // Agrega este método para que el navegador (GET) pueda entrar
    @GetMapping
    public ResponseEntity<List<ActividadDTO>> listarTodas() {
        // Necesitas crear 'listarTodas' en tu ActividadService también
        return ResponseEntity.ok(actividadService.listarTodas());
    }

    @GetMapping("/mis-tareas")
    public ResponseEntity<List<ActividadDTO>> listarMisActividades(Principal principal) {
        // principal.getName() nos da el email del usuario logueado
        return ResponseEntity.ok(actividadService.listarPorEmailDesarrollador(principal.getName()));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('DESARROLLADOR', 'LIDER', 'COORDINADOR')")
    public ResponseEntity<ActividadDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null) {
            throw new RuntimeException("El campo 'estado' es obligatorio");
        }

        return ResponseEntity.ok(actividadService.cambiarEstadoActividad(id, nuevoEstado));
    }

}