package com.pgcg.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Publicacion;

@Repository
public interface IPublicacionRepo extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findByEliminadaFalseOrderByIdAsc();

    List<Publicacion> findByPropiedadIdAndEliminadaFalse(Long propiedadId);

    boolean existsByPropiedadIdAndEstadoAndEliminadaFalse(Long propiedadId, EstadoPublicacion estado);

    boolean existsByPropiedadIdAndEstadoAndEliminadaFalseAndIdNot(Long propiedadId, EstadoPublicacion estado, Long id);
}
