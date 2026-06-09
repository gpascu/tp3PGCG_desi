package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Contrato;

@Repository
public interface IContratoRepo extends JpaRepository<Contrato, Long> {
    java.util.List<Contrato> findByEliminadoFalseOrderByIdAsc();
}
