package com.pgcg.servicios;

import com.pgcg.entidades.EstadoDisponibilidad;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Propiedad;
import com.pgcg.entidades.Publicacion;
import com.pgcg.accesoDatos.IPublicacionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        // la propiedad está "disponible" 
        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
            throw new RuntimeException("Error: Debe asociar una propiedad válida a la publicación.");
        }
        
        Long propiedadId = publicacion.getPropiedad().getId();
        Propiedad propiedadReal = propiedadService.buscarPorId(propiedadId);
        
        if (publicacion.getId() == null) {
            boolean yaExisteActiva = IPublicacionRepo.existsByPublicacionIdAndEstadoAndEliminadaFalse(propiedadId, EstadoPublicacion.ACTIVA);
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
		return IPublicacionRepo.findByEliminadaFalseOrderByIdAsc();
	}

	@Override
	public Publicacion buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
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

	public List<Publicacion> buscarConFiltros(Long id, EstadoPublicacion estado) {
	    //  busca por ID
	    if (id != null) {
	        try {
	            return publicacionRepo.findById(id)
	                    .map(p -> List.of(p)) 
	                    .orElse(List.of());
	        } catch (Exception e) {
	            // Si hay un error de base de datos, lo imprime y retorna una lista vacía 
	            e.printStackTrace();
	            return List.of(); 
	        }
	    }
	    
	    // filtra por estado --> ESTO NO ME FUNCIONA. Guille, lo podes mirar y comparar con el tuyo?
	    if (estado != null) {
	        //LÍNEAS PARA DEPURAR:
	        System.out.println("=== DEBBUGIN: Buscando en la BD publicaciones con estado: " + estado);
	        
	        List<Publicacion> resultadoBD = IPublicacionRepo.findByEstado(estado);
	        
	        System.out.println("=== DEBBUGIN: La base de datos devolvió: " + resultadoBD.size() + " registros.");
	        
	        return resultadoBD; 
	    }
	    
	    // 3. Si apretó "Buscar" con todos los filtros vacíos, trae absolutamente todo
	    return publicacionRepo.findAll();
	}
   
}