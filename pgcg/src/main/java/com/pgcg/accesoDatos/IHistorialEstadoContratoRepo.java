package com.pgcg.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.HistorialEstadoContrato;

@Repository
public interface IHistorialEstadoContratoRepo extends JpaRepository<HistorialEstadoContrato, Long> {
    List<HistorialEstadoContrato> findByContratoIdOrderByFechaHoraDesc(Long contratoId);
}
