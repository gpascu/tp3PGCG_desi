package com.pgcg.servicios;

import com.pgcg.entidades.EstadoDisponibilidad;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Propiedad;
import com.pgcg.entidades.Publicacion;
import com.pgcg.accesoDatos.IPublicacionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private IPublicacionRepo publicacionRepo;

    @Autowired
    private PropiedadService propiedadService; 

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
            return publicacionRepo.save(publicacion);
        } else {
            // ----- EDICIÓN -----
            Publicacion existente = publicacionRepo.findById(publicacion.getId())
                    .orElseThrow(() -> new RuntimeException("Error: No existe la publicación a editar."));

            // No se puede reactivar una publicación finalizada
            if (existente.getEstado() == EstadoPublicacion.FINALIZADA
                    && publicacion.getEstado() != EstadoPublicacion.FINALIZADA) {
                throw new RuntimeException("Error: No se puede reactivar una publicación finalizada.");
            }
            // No dejar dos publicaciones ACTIVAS de la misma propiedad
            if (publicacion.getEstado() == EstadoPublicacion.ACTIVA
                    && publicacionRepo.existsByPropiedadIdAndEstadoAndEliminadaFalseAndIdNot(
                            propiedadId, EstadoPublicacion.ACTIVA, existente.getId())) {
                throw new RuntimeException("Error: La propiedad ya tiene otra publicación ACTIVA.");
            }

            // La propiedad asociada no se cambia al editar; se conserva la original
            existente.setPrecioMensual(publicacion.getPrecioMensual());
            existente.setCondiciones(publicacion.getCondiciones());
            existente.setFechaPublicacion(publicacion.getFechaPublicacion());
            existente.setDescripcion(publicacion.getDescripcion());
            existente.setEstado(publicacion.getEstado());
            return publicacionRepo.save(existente);
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
				publicacionRepo.save(p);
			}
		}
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
	public List<Publicacion> buscarConFiltros(Long id, EstadoPublicacion estado) {
	    List<Publicacion> todas = publicacionRepo.findByEliminadaFalseOrderByIdAsc();
	    List<Publicacion> resultado = new ArrayList<Publicacion>();
	    if (todas == null) {
	        return resultado;
	    }

	    for (Publicacion p : todas) {
	        boolean cumple = true;
	        
	        // Filtro por ID
	        if (id != null && (p.getId() == null || !p.getId().equals(id))) {
	            cumple = false;
	        }
	        
	        // Filtro por Estado
	        if (estado != null && p.getEstado() != estado) {
	            cumple = false;
	        }

	        // Si paso todos los filtros activos se agrega al resultado
	        if (cumple) {
	            resultado.add(p);
	        }
	    }
	    
	    return resultado;
	}
   
}