package com.ikernell.backend.repository;

import com.ikernell.backend.entity.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtapaRepository extends JpaRepository<Etapa, Long> {
    // Para listar las etapas de un solo proyecto
    List<Etapa> findByProyectoIdProyecto(Long idProyecto);
}