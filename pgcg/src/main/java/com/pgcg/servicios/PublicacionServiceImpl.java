package com.pgcg.servicios;

import com.pgcg.entidades.EstadoDisponibilidad;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Propiedad;
import com.pgcg.entidades.Publicacion;

import jakarta.transaction.Transactional;

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
    @Transactional
    public Publicacion guardar(Publicacion publicacion) {
        
        // precio mensual 
        if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Error: El precio mensual de la publicación debe ser mayor a $0.");
        }

        // la propiedad está "disponible" 
        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
            throw new RuntimeException("Error: Debe asociar una propiedad válida a la publicación.");
        }
        
        Long propiedadId = publicacion.getPropiedad().getId();
        Propiedad propiedadReal = propiedadService.buscarPorId(propiedadId);
        
        if (publicacion.getId() == null) {
            boolean yaExisteActiva = publicacionRepo.existsByPropiedadIdAndEstadoAndEliminadaFalse(propiedadId, EstadoPublicacion.ACTIVA);
            if (yaExisteActiva) {
                throw new RuntimeException("Error: La propiedad ya tiene una publicación ACTIVA en el sistema.");
            }
        }

        if (propiedadReal == null) {
            throw new RuntimeException("Error: La propiedad con ID " + propiedadId + " no existe.");
        }

        if (propiedadReal.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
            throw new RuntimeException("Error: No se puede publicar. La propiedad no se encuentra en estado DISPONIBLE.");
        }

        publicacion.setPropiedad(propiedadReal);


       
        if (publicacion.getId() == null) {
            publicacion.setEstado(EstadoPublicacion.ACTIVA);
            publicacion.setEliminada(false);
        }

  
        return publicacionRepo.save(publicacion);
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
	    List<Publicacion> todas = publicacionRepo.findAll(); 
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
   
	public Publicacion modificar(Long id, Publicacion publicacionModificada) {
        // buscar publicacion
        Publicacion publicacionExistente = publicacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La publicación con ID " + id + " no existe."));

        // precio mensual
        if (publicacionModificada.getPrecioMensual() == null || publicacionModificada.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Error: El precio mensual de la publicación debe ser mayor a $0.");
        }

        // validar y asociar la propiedad
        if (publicacionModificada.getPropiedad() == null || publicacionModificada.getPropiedad().getId() == null) {
            throw new RuntimeException("Error: Debe asociar una propiedad válida a la publicación.");
        }
        
        Long propiedadId = publicacionModificada.getPropiedad().getId();
        Propiedad propiedadReal = propiedadService.buscarPorId(propiedadId);
        
        if (propiedadReal == null) {
            throw new RuntimeException("Error: La propiedad con ID " + propiedadId + " no existe.");
        }
        
        if (propiedadReal.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
            throw new RuntimeException("Error: No se puede publicar. La propiedad no se encuentra en estado DISPONIBLE.");
        }

        // actualizar datos
        publicacionExistente.setDescripcion(publicacionModificada.getDescripcion());
        publicacionExistente.setPrecioMensual(publicacionModificada.getPrecioMensual());
        publicacionExistente.setPropiedad(propiedadReal);
        
        //  estado 
        if (publicacionModificada.getEstado() != null) {
            publicacionExistente.setEstado(publicacionModificada.getEstado());
        }

        return publicacionRepo.save(publicacionExistente);
    }
}