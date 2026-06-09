package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.HistorialEstadoIncidente;

@Repository
public interface IHistorialEstadoIncidenteRepo extends JpaRepository<HistorialEstadoIncidente, Long> {}
