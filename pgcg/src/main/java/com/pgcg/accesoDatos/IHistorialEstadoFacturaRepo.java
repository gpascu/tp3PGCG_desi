package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.HistorialEstadoFactura;

@Repository
public interface IHistorialEstadoFacturaRepo extends JpaRepository<HistorialEstadoFactura, Long> {}
