package com.ikernell.backend.repository;

import com.ikernell.backend.entity.AuditoriaPresupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<AuditoriaPresupuesto, Long> {
    List<AuditoriaPresupuesto> findByProyectoIdProyectoOrderByFechaCambioDesc(Long idProyecto);
}