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
        
        // VALIDACIÓN 1: El precio mensual debe ser mayor a cero (HU 2.1)
        if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Error: El precio mensual de la publicación debe ser mayor a $0.");
        }

        // VALIDACIÓN 2: Verificar si la propiedad está "disponible" (Criterio de Aceptación HU 2.1)
        // Primero validamos que nos hayan enviado una propiedad en la petición
        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null) {
            throw new RuntimeException("Error: Debe asociar una propiedad válida a la publicación.");
        }
        
        // Buscamos los datos reales de la propiedad usando el servicio de tu compañero
        Long propiedadId = publicacion.getPropiedad().getId();
        Propiedad propiedadReal = propiedadService.buscarPorId(propiedadId);
        
        if (publicacion.getId() == null) {
            boolean yaExisteActiva = IPublicacionRepo.existsByPropiedadIdAndEstadoAndEliminadaFalse(propiedadId, EstadoPublicacion.ACTIVA);
            if (yaExisteActiva) {
                throw new RuntimeException("Error: La propiedad ya tiene una publicación ACTIVA en el sistema.");
            }
        }

        if (propiedadReal == null) {
            throw new RuntimeException("Error: La propiedad con ID " + propiedadId + " no existe.");
        }

        // Verificamos el estado de disponibilidad 
        if (propiedadReal.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
            throw new RuntimeException("Error: No se puede publicar. La propiedad no se encuentra en estado DISPONIBLE.");
        }

        // Si pasó todas las validaciones
        publicacion.setPropiedad(propiedadReal);


        // LÓGICA DE ALTA NUEVA (Cuando el id es nulo)
        if (publicacion.getId() == null) {
            publicacion.setEstado(EstadoPublicacion.ACTIVA);
            publicacion.setEliminada(false);
        }

        // Guardamos finalmente en la base de datos
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
	    // 1. Buscamos la publicación real en la base de datos
	    Publicacion publicacion = publicacionRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Error: No se encontró la publicación con el ID " + id));

	    // 2. CRITERIO DE ACEPTACIÓN: Validar que esté en estado ACTIVA 
	    if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
	        throw new RuntimeException("Error: Solo se pueden dar de baja publicaciones que se encuentren en estado ACTIVA.");
	    }

	    // 3. APLICAR BAJA LÓGICA: Cambiamos el flag a true en vez de borrar el registro
	    publicacion.setEliminada(true);

	    // 4. Guardamos los cambios para actualizar la fila en MySQL
	    publicacionRepo.save(publicacion);
	}
   
}