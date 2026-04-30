package com.ikernell.backend.controller;

import com.ikernell.backend.dto.EtapaDTO;
import com.ikernell.backend.entity.Etapa;
import com.ikernell.backend.service.EtapaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etapas")
public class EtapaController {

    private final EtapaService etapaService;

    public EtapaController(EtapaService etapaService) {
        this.etapaService = etapaService;
    }

    @PostMapping
    public ResponseEntity<EtapaDTO> crear(@RequestBody Etapa etapa) {
        return new ResponseEntity<>(etapaService.guardar(etapa), HttpStatus.CREATED);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<List<EtapaDTO>> listarPorProyecto(@PathVariable Long idProyecto) {
        return ResponseEntity.ok(etapaService.listarPorProyecto(idProyecto));
    }
}