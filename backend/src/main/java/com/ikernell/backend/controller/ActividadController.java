package com.ikernell.backend.controller;

import com.ikernell.backend.dto.ActividadDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.service.ActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}