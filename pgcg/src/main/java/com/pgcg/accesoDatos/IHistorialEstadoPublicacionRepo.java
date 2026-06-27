package com.pgcg.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.HistorialEstadoPublicacion;

@Repository
public interface IHistorialEstadoPublicacionRepo extends JpaRepository<HistorialEstadoPublicacion, Long> {
	
	List<HistorialEstadoPublicacion> findByPublicacionIdOrderByFechaHoraDesc(Long publicacionId);
}
