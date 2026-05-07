package com.ikernell.backend.service;

import com.ikernell.backend.dto.ProyectoDTO; // Import para usar el DTO de Proyecto
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.AuditoriaPresupuesto;
import com.ikernell.backend.entity.Etapa; // Import para usar la entidad Etapa
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.ProyectoRepository;
import com.ikernell.backend.repository.UsuarioRepository; // Import para usar el repositorio de Usuario
import com.ikernell.backend.repository.EtapaRepository;
import com.ikernell.backend.repository.ActividadRepository;
import org.springframework.stereotype.Service; // Import para usar la anotación @Service
import com.ikernell.backend.repository.NotificacionRepository; // Import para usar el repositorio de Notificaciones
import jakarta.transaction.Transactional; // Import para usar la anotación @Transactional

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List; // Import para usar List
import java.util.stream.Collectors; // Import para usar Stream y Collectors
import java.util.Map; // Import para usar Map
import com.ikernell.backend.repository.AuditoriaRepository; // Import para usar el repositorio de Auditoría

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository; // Repositorio para acceder a los datos de proyectos
    private final UsuarioRepository usuarioRepository; // Repositorio para acceder a los datos de usuarios (líderes)
    private final EtapaRepository etapaRepository; // Repositorio para acceder a los datos de etapas
    private final ActividadRepository actividadRepository; // Repositorio para acceder a los datos de actividades
    private final NotificacionRepository notificacionRepository; // Repositorio para acceder a los datos de
                                                                 // notificaciones
    private final AuditoriaRepository auditoriaRepository; // Repositorio para acceder a los datos

    public ProyectoService(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository,
            EtapaRepository etapaRepository, ActividadRepository actividadRepository,
            NotificacionRepository notificacionRepository, AuditoriaRepository auditoriaRepository) { // Inyección de
                                                                                                      // dependencias
        // para los repositorios
        this.proyectoRepository = proyectoRepository; // Inyección de dependencias para ProyectoRepository
        this.usuarioRepository = usuarioRepository; // Inyección de dependencias para UsuarioRepository
        this.etapaRepository = etapaRepository;// Inyección de dependencias para EtapaRepository
        this.actividadRepository = actividadRepository;// Inyección de dependencias para ActividadRepository
        this.notificacionRepository = notificacionRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    public ProyectoDTO guardar(ProyectoDTO dto) {
        Proyecto proyecto = convertirAEntidad(dto);

        Usuario lider = usuarioRepository.findById(dto.getIdLider())
                .orElseThrow(() -> new RuntimeException("El Líder no existe"));

        proyecto.setLider(lider);
        Proyecto guardado = proyectoRepository.save(proyecto);
        return convertirADto(guardado);
    }

    // 2. Método para actualizar (Corrije el error del Controlador)
    @Transactional
    public ProyectoDTO actualizar(Long id, ProyectoDTO dto, String emailAutor) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        // 1. Auditoría de Presupuesto
        if (!proyecto.getPresupuesto().equals(dto.getPresupuesto())) {
            AuditoriaPresupuesto registro = new AuditoriaPresupuesto();
            registro.setMontoAnterior(proyecto.getPresupuesto());
            registro.setMontoNuevo(dto.getPresupuesto());
            registro.setFechaCambio(LocalDateTime.now());
            registro.setUsuarioResponsable(emailAutor);
            registro.setProyecto(proyecto);
            registro.setMotivo(dto.getMotivoCambio() != null ? dto.getMotivoCambio() : "Actualización manual");

            auditoriaRepository.save(registro);
        }
        proyecto.setNombre(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setPresupuesto(dto.getPresupuesto());
        proyecto.setEstado(dto.getEstado());
        proyecto.setFechaInicio(dto.getFechaInicio());
        proyecto.setFechaFin(dto.getFechaFin());

        return convertirADto(proyectoRepository.save(proyecto));
    }

    // 3. Método auxiliar para convertir DTO a Entidad
    private Proyecto convertirAEntidad(ProyectoDTO dto) {
        Proyecto p = new Proyecto();
        p.setNombre(dto.getNombre());
        p.setPresupuesto(dto.getPresupuesto());
        p.setEstado(dto.getEstado());
        p.setDescripcion(dto.getDescripcion());
        p.setPresupuesto(dto.getPresupuesto());
        p.setEstado(dto.getEstado());
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        return p;
    }

    public List<ProyectoDTO> listarTodos() {
        return proyectoRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public Double calcularCostoTotal(Long idProyecto) {
        // 1. Buscamos las etapas asociadas al proyecto
        List<Etapa> etapas = etapaRepository.findByProyectoIdProyecto(idProyecto);

        // 2. Procesamos el cálculo con tipos claros
        return etapas.stream()
                .flatMap(etapa -> {
                    // Obtenemos la lista de actividades para esta etapa
                    List<Actividad> actividades = actividadRepository.findByEtapaIdEtapa(etapa.getIdEtapa());
                    // La convertimos explícitamente a Stream
                    return actividades.stream();
                })
                .mapToDouble(actividad -> {
                    // Validamos nulos para evitar errores en tiempo de ejecución
                    return actividad.getCostoEstimado() != null ? actividad.getCostoEstimado() : 0.0;
                })
                .sum();
    }

    private ProyectoDTO convertirADto(Proyecto p) {
        // Mapeo manual de campos del proyecto
        ProyectoDTO dto = new ProyectoDTO();
        dto.setIdProyecto(p.getIdProyecto());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setEstado(p.getEstado());

        // Cálculo del costo total del proyecto
        dto.setCostoTotal(this.calcularCostoTotal(p.getIdProyecto()));
        // Mapeo manual de datos del líder
        if (p.getLider() != null) {
            dto.setIdLider(p.getLider().getIdUsuario());
            dto.setNombreLider(p.getLider().getNombre() + " " + p.getLider().getApellido());
        }
        return dto;
    }

    public Map<String, Object> obtenerBalanceCuentas(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        double costoActual = calcularCostoTotal(id);

        Map<String, Object> balance = new HashMap<>();
        balance.put("nombreProyecto", proyecto.getNombre());
        balance.put("presupuestoTotal", proyecto.getPresupuesto());
        balance.put("costoConsumido", costoActual);
        balance.put("saldoDisponible", proyecto.getPresupuesto() - costoActual);
        return balance;
    }

    public List<AuditoriaPresupuesto> obtenerHistorialFinanciero(Long idProyecto) {
        return auditoriaRepository.findByProyectoIdProyectoOrderByFechaCambioDesc(idProyecto);
    }
}