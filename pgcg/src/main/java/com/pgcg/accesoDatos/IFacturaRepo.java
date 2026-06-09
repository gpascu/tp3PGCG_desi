package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Factura;

@Repository
public interface IFacturaRepo extends JpaRepository<Factura, Long> {
    java.util.List<Factura> findByEliminadaFalseOrderByIdAsc();
}
