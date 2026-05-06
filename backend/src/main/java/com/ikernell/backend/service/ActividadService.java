package com.ikernell.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ikernell.backend.dto.ActividadDTO;
import com.ikernell.backend.dto.AlertaPresupuestoDTO;
import com.ikernell.backend.dto.RankingDesarrolladorDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.Etapa;
import com.ikernell.backend.entity.Notificacion;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.exception.PresupuestoExcedidoException;
import com.ikernell.backend.repository.ActividadRepository;
import com.ikernell.backend.repository.EtapaRepository;
import com.ikernell.backend.repository.ProyectoRepository;
import com.ikernell.backend.repository.UsuarioRepository;
import com.ikernell.backend.repository.NotificacionRepository;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final EtapaRepository etapaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoRepository proyectoRepository; // <--- 1. Declarar el repositorio
    private final NotificacionRepository notificacionRepository;

    public ActividadService(ActividadRepository actividadRepository, EtapaRepository etapaRepository,
            UsuarioRepository usuarioRepository, ProyectoRepository proyectoRepository, NotificacionRepository notificacionRepository) {
        this.actividadRepository = actividadRepository;
        this.etapaRepository = etapaRepository;
        this.usuarioRepository = usuarioRepository;
        this.proyectoRepository = proyectoRepository; // <--- 2. Inicializar el repositorio en el constructor
        this.notificacionRepository = notificacionRepository;

    }

    public AlertaPresupuestoDTO verificarRiesgoPresupuesto(Long idProyecto, Double nuevoCosto) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        double costoActual = proyecto.getEtapas().stream()
                .flatMap(etapa -> etapa.getActividades().stream())
                .mapToDouble(Actividad::getCostoEstimado)
                .sum();

        // Validar división por cero si el presupuesto es 0
        double presupuestoTotal = proyecto.getPresupuesto() > 0 ? proyecto.getPresupuesto() : 1;
        double porcentajeConsumido = ((costoActual + nuevoCosto) / presupuestoTotal) * 100;
        double saldoRestante = proyecto.getPresupuesto() - (costoActual + nuevoCosto);

        AlertaPresupuestoDTO alerta = new AlertaPresupuestoDTO();
        alerta.setPorcentajeConsumido(porcentajeConsumido);
        alerta.setSaldoRestante(saldoRestante);

        if (porcentajeConsumido > 100) {
            alerta.setMensaje("¡CRÍTICO! El costo excede el presupuesto total del proyecto.");
            alerta.setRiesgoAlto(true);
        } else if (porcentajeConsumido >= 80) {
            alerta.setMensaje("ADVERTENCIA: El proyecto ha consumido el " + String.format("%.2f", porcentajeConsumido)
                    + "% del presupuesto.");
            alerta.setRiesgoAlto(true);
        } else {
            alerta.setMensaje("Presupuesto dentro de límites aceptables.");
            alerta.setRiesgoAlto(false);
        }

        return alerta;
    }

    // 3. Método para convertir DTO a Entidad (Soluciona el error de undefined)
    private Actividad convertirAEntidad(ActividadDTO dto) {
        Actividad a = new Actividad();
        a.setDescripcion(dto.getDescripcion());
        a.setEstado(dto.getEstado());
        a.setCostoEstimado(dto.getCostoEstimado());
        a.setFechaEntregaPlaneada(dto.getFechaEntregaPlaneada());

        Etapa etapa = etapaRepository.findById(dto.getIdEtapa())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));
        Usuario dev = usuarioRepository.findById(dto.getIdDesarrollador())
                .orElseThrow(() -> new RuntimeException("Desarrollador no encontrado"));

        a.setEtapa(etapa);
        a.setDesarrollador(dev);
        return a;
    }

    public ActividadDTO guardarActividad(ActividadDTO dto) {
        // Aquí es donde se llama al método que acabas de crear
        AlertaPresupuestoDTO alerta = verificarRiesgoPresupuesto(dto.getIdProyecto(), dto.getCostoEstimado());

        // Si el riesgo es crítico (>100%), lanzamos la excepción que ya tenías
        if (alerta.getPorcentajeConsumido() > 100) {
            throw new PresupuestoExcedidoException(alerta.getMensaje());
        }

        // Si es riesgo alto (80-100%), podemos imprimirlo o loguearlo
        if (alerta.isRiesgoAlto()) {
            crearNotificacionPresupuesto(dto.getIdProyecto(), alerta.getMensaje());
        }

        return convertirADto(actividadRepository.save(convertirAEntidad(dto)));
    }

    public List<ActividadDTO> listarPorEtapa(Long idEtapa) {
        return actividadRepository.findByEtapaIdEtapa(idEtapa)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarTodas() {
        return actividadRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ActividadDTO convertirADto(Actividad a) {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdActividad(a.getIdActividad());
        dto.setDescripcion(a.getDescripcion());
        dto.setEstado(a.getEstado());
        dto.setCostoEstimado(a.getCostoEstimado());
        dto.setNombreEtapa(a.getEtapa().getNombreEtapa());
        dto.setNombreDesarrollador(a.getDesarrollador().getNombre() + " " + a.getDesarrollador().getApellido());
        return dto;
    }

    public List<ActividadDTO> listarPorEmailDesarrollador(String email) {
        return actividadRepository.findByDesarrolladorEmail(email)
                .stream()
                .map(this::convertirADto) // Reutiliza tu método de conversión
                .collect(Collectors.toList());
    }

    private void actualizarEstadosSuperiores(Etapa etapa) {
        // 1. Verificar actividades de la etapa
        boolean todasActividadesListas = etapa.getActividades().stream()
                .allMatch(a -> "Terminado".equalsIgnoreCase(a.getEstado()));

        if (todasActividadesListas) {
            etapa.setEstado("Finalizada");
            etapa.setFechaFinReal(LocalDateTime.now()); // <--- AUDITORÍA DE ETAPA
            etapaRepository.save(etapa);

            // 2. Verificar etapas del proyecto
            Proyecto proyecto = etapa.getProyecto();
            boolean todasEtapasListas = proyecto.getEtapas().stream()
                    .allMatch(e -> "Finalizada".equalsIgnoreCase(e.getEstado()));

            if (todasEtapasListas) {
                proyecto.setEstado("Completado");
                proyecto.setFechaFinReal(LocalDateTime.now()); // <--- AUDITORÍA DE PROYECTO
                proyectoRepository.save(proyecto);
            }
        }
    }

    public ActividadDTO cambiarEstadoActividad(Long idActividad, String nuevoEstado) {
        // 1. Buscar la actividad
        Actividad actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        // 2. Actualizar estado
        actividad.setEstado(nuevoEstado);
        Actividad guardada = actividadRepository.save(actividad);

        // 3. Disparar lógica de workflow si se termina la tarea
        if ("Terminado".equalsIgnoreCase(nuevoEstado)) {
            actualizarEstadosSuperiores(actividad.getEtapa());
        }

        return convertirADto(guardada);
    }

    public double calcularPuntajeEficiencia(Actividad actividad) {
        if (actividad.getFechaEntregaPlaneada() == null || !"Terminado".equalsIgnoreCase(actividad.getEstado())) {
            return 0.0;
        }

        LocalDateTime fechaReal = LocalDateTime.now(); // Se asume que se llama al terminar

        // Si terminó antes o justo a tiempo, el puntaje es positivo
        if (fechaReal.isBefore(actividad.getFechaEntregaPlaneada())
                || fechaReal.isEqual(actividad.getFechaEntregaPlaneada())) {
            return 100.0; // Entrega perfecta o adelantada
        } else {
            // Si se retrasó, restamos puntos por cada hora de retraso (ejemplo simple)
            long horasRetraso = java.time.Duration.between(actividad.getFechaEntregaPlaneada(), fechaReal).toHours();
            double penalizacion = horasRetraso * 2.0; // 2 puntos menos por cada hora
            return Math.max(0, 100.0 - penalizacion);
        }
    }

    // Método para obtener el ranking de eficiencia de los desarrolladores
    public List<RankingDesarrolladorDTO> obtenerRankingEficiencia() {
        // 1. Obtener todos los usuarios que son desarrolladores
        List<Usuario> desarrolladores = usuarioRepository.findAll();

        return desarrolladores.stream()
                .map(dev -> {
                    // 2. Filtrar actividades terminadas de este desarrollador
                    List<Actividad> terminadas = actividadRepository.findByDesarrolladorEmail(dev.getEmail())
                            .stream()
                            .filter(a -> "Terminado".equalsIgnoreCase(a.getEstado()))
                            .collect(Collectors.toList());

                    if (terminadas.isEmpty()) {
                        return new RankingDesarrolladorDTO(dev.getNombre() + " " + dev.getApellido(), 0.0, 0);
                    }

                    // 3. Calcular el promedio de eficiencia
                    double promedio = terminadas.stream()
                            .mapToDouble(this::calcularPuntajeEficiencia)
                            .average()
                            .orElse(0.0);

                    return new RankingDesarrolladorDTO(
                            dev.getNombre() + " " + dev.getApellido(),
                            Math.round(promedio * 100.0) / 100.0, // Redondear a 2 decimales
                            terminadas.size());
                })
                .sorted((r1, r2) -> Double.compare(r2.getPuntajePromedio(), r1.getPuntajePromedio())) // Ordenar de
                                                                                                      // mayor a menor
                .collect(Collectors.toList());
    }

    private void crearNotificacionPresupuesto(Long idProyecto, String mensaje) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        if (proyecto != null) {
            Notificacion n = new Notificacion();
            n.setMensaje(mensaje + " Proyecto: " + proyecto.getNombre());
            n.setFechaCreacion(LocalDateTime.now());
            n.setTipo("PRESUPUESTO");
            // Aquí podrías buscar al LIDER asignado al proyecto
            // Por ahora, lo asignaremos al usuario que creó el proyecto
            n.setDestinatario(proyecto.getLider());
            notificacionRepository.save(n);
        }
    }
}