package com.pgcg.servicios;

import com.pgcg.entidades.EstadoDisponibilidad;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.HistorialEstadoPublicacion;
import com.pgcg.entidades.Propiedad;
import com.pgcg.entidades.Publicacion;
import com.pgcg.accesoDatos.IPublicacionRepo;
import com.pgcg.accesoDatos.IHistorialEstadoPublicacionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private IPublicacionRepo publicacionRepo;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private IHistorialEstadoPublicacionRepo historialRepo;

    @Override
    public Publicacion guardar(Publicacion publicacion) {

        // precio mensual
        if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Error: El precio mensual de la publicación debe ser mayor a $0.");
        }

        // debe asociar una propiedad válida
        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
            throw new RuntimeException("Error: Debe asociar una propiedad válida a la publicación.");
        }

        Long propiedadId = publicacion.getPropiedad().getId();
        Propiedad propiedadReal = propiedadService.buscarPorId(propiedadId);
        if (propiedadReal == null) {
            throw new RuntimeException("Error: La propiedad con ID " + propiedadId + " no existe.");
        }

        // Todos los datos de la publicación son requeridos
        if (publicacion.getCondiciones() == null || publicacion.getCondiciones().trim().isEmpty()) {
            throw new RuntimeException("Error: Las condiciones de alquiler son obligatorias.");
        }
        if (publicacion.getDescripcion() == null || publicacion.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("Error: La descripción es obligatoria.");
        }
        if (publicacion.getFechaPublicacion() == null) {
            throw new RuntimeException("Error: La fecha de publicación es obligatoria.");
        }

        if (publicacion.getId() == null) {
            // ----- ALTA -----
            // No puede haber dos publicaciones ACTIVAS de la misma propiedad
            if (publicacionRepo.existsByPropiedadIdAndEstadoAndEliminadaFalse(propiedadId, EstadoPublicacion.ACTIVA)) {
                throw new RuntimeException("Error: La propiedad ya tiene una publicación ACTIVA en el sistema.");
            }
            // Solo se puede publicar una propiedad disponible
            if (propiedadReal.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                throw new RuntimeException("Error: No se puede publicar. La propiedad no se encuentra en estado DISPONIBLE.");
            }
            publicacion.setPropiedad(propiedadReal);
            publicacion.setEstado(EstadoPublicacion.ACTIVA);
            publicacion.setEliminada(false);
            Publicacion guardada = publicacionRepo.save(publicacion);
            // Se registra el estado inicial en el historial
            guardarHistorial(guardada, guardada.getEstado());
            return guardada;
        } else {
            // ----- EDICIÓN -----
            Publicacion existente = publicacionRepo.findById(publicacion.getId())
                    .orElseThrow(() -> new RuntimeException("Error: No existe la publicación a editar."));

            // No se puede editar una publicación finalizada
            if (existente.getEstado() == EstadoPublicacion.FINALIZADA) {
                throw new RuntimeException("Error: No se puede modificar una publicación que ya se encuentra FINALIZADA.");
            }
            
            // Vuelvo a chequear disponibilidad de la prop para cambio de estado
            if (publicacion.getEstado() == EstadoPublicacion.ACTIVA) {
                if (propiedadReal.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new RuntimeException("Error: No se puede activar la publicación. La propiedad asociada no se encuentra en estado DISPONIBLE.");
                } 
            }
            // No dejar dos publicaciones ACTIVAS de la misma propiedad
            if (publicacion.getEstado() == EstadoPublicacion.ACTIVA
                    && publicacionRepo.existsByPropiedadIdAndEstadoAndEliminadaFalseAndIdNot(
                            propiedadId, EstadoPublicacion.ACTIVA, existente.getId())) {
                throw new RuntimeException("Error: La propiedad ya tiene otra publicación ACTIVA.");
            }

            // La propiedad asociada no se cambia al editar; se conserva la original
            EstadoPublicacion estadoAnterior = existente.getEstado();
            existente.setPrecioMensual(publicacion.getPrecioMensual());
            existente.setCondiciones(publicacion.getCondiciones());
            existente.setFechaPublicacion(publicacion.getFechaPublicacion());
            existente.setDescripcion(publicacion.getDescripcion());
            existente.setEstado(publicacion.getEstado());
            Publicacion guardada = publicacionRepo.save(existente);
            // Si cambió el estado, se registra en el historial
            if (estadoAnterior != guardada.getEstado()) {
                guardarHistorial(guardada, guardada.getEstado());
            }
            return guardada;
        }
    }

	@Override
	public List<Publicacion> listarTodas() {
		return publicacionRepo.findByEliminadaFalseOrderByIdAsc();
	}

	@Override
	public Publicacion buscarPorId(Long id) {
		return publicacionRepo.findById(id).orElse(null);
	}

	@Override
	public void finalizarPublicacionesDePropiedad(Long propiedadId) {
		if (propiedadId == null) {
			return;
		}
		List<Publicacion> publicaciones = publicacionRepo.findByPropiedadIdAndEliminadaFalse(propiedadId);
		for (Publicacion p : publicaciones) {
			if (p.getEstado() != EstadoPublicacion.FINALIZADA) {
				p.setEstado(EstadoPublicacion.FINALIZADA);
				Publicacion guardada = publicacionRepo.save(p);
				guardarHistorial(guardada, EstadoPublicacion.FINALIZADA);
			}
		}
	}

	// Guarda un registro en el historial con el estado y la fecha/hora del cambio
	private void guardarHistorial(Publicacion publicacion, EstadoPublicacion estado) {
		HistorialEstadoPublicacion h = new HistorialEstadoPublicacion();
		h.setPublicacion(publicacion);
		h.setEstado(estado);
		h.setFechaHora(LocalDateTime.now());
		historialRepo.save(h);
	}
	
	@Override
	
	public void eliminar(Long id) {
	   
	    Publicacion publicacion = publicacionRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Error: No se encontró la publicación con el ID " + id));

	    
	    if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
	        throw new RuntimeException("Error: Solo se pueden dar de baja publicaciones que se encuentren en estado ACTIVA.");
	    }

	    publicacion.setEliminada(true);

	    publicacionRepo.save(publicacion);
	}

	@Override
	public List<Publicacion> buscarConFiltros(Long propiedadId, Long ciudadId, EstadoPublicacion estado,
	        BigDecimal precioMin, BigDecimal precioMax) {
	    List<Publicacion> todas = publicacionRepo.findByEliminadaFalseOrderByIdAsc();
	    List<Publicacion> resultado = new ArrayList<Publicacion>();
	    if (todas == null) {
	        return resultado;
	    }

	    for (Publicacion p : todas) {
	        boolean cumple = true;
	        Propiedad prop = p.getPropiedad();

	        // Filtro por propiedad
	        if (propiedadId != null && (prop == null || !propiedadId.equals(prop.getId()))) {
	            cumple = false;
	        }
	        // Filtro por ciudad de la propiedad
	        if (ciudadId != null && (prop == null || prop.getCiudad() == null || !ciudadId.equals(prop.getCiudad().getId()))) {
	            cumple = false;
	        }
	        // Filtro por estado
	        if (estado != null && p.getEstado() != estado) {
	            cumple = false;
	        }
	        // Filtro por rango de precio mensual
	        if (precioMin != null && (p.getPrecioMensual() == null || p.getPrecioMensual().compareTo(precioMin) < 0)) {
	            cumple = false;
	        }
	        if (precioMax != null && (p.getPrecioMensual() == null || p.getPrecioMensual().compareTo(precioMax) > 0)) {
	            cumple = false;
	        }

	        if (cumple) {
	            resultado.add(p);
	        }
	    }

	    return resultado;
	}

}