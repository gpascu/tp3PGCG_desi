package com.pgcg.accesoDatos;

import com.pgcg.entidades.Publicacion;
import com.pgcg.entidades.EstadoPublicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPublicacionRepo extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByEliminadaFalseOrderByIdAsc();
    boolean existsByPropiedadIdAndEstadoAndEliminadaFalse(Long propiedadId, EstadoPublicacion estado);
}