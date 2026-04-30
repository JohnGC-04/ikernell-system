package com.ikernell.backend.repository;

import com.ikernell.backend.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    // Para ver qué tareas hay en una fase específica
    List<Actividad> findByEtapaIdEtapa(Long idEtapa);
    
    // Para ver qué tareas tiene asignadas un desarrollador (HU-005)
    List<Actividad> findByDesarrolladorIdUsuario(Long idUsuario);
}