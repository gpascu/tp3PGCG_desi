package com.pgcg.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Publicacion;

@Repository
public interface IPublicacionRepo extends JpaRepository<Publicacion, Long> {
    static java.util.List<Publicacion> findByEliminadaFalseOrderByIdAsc() {
		// TODO Auto-generated method stub
		return null;
	}

	static boolean existsByPublicacionIdAndEstadoAndEliminadaFalse(Long publicaciondId, EstadoPublicacion activa) {
		// TODO Auto-generated method stub
		return false;
	}



}
