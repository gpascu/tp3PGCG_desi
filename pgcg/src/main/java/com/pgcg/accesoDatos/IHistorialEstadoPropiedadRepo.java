package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.HistorialEstadoPropiedad;

@Repository
public interface IHistorialEstadoPropiedadRepo extends JpaRepository<HistorialEstadoPropiedad, Long> {}
