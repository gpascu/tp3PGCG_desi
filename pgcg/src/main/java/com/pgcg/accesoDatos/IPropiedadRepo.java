package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Propiedad;

@Repository
public interface IPropiedadRepo extends JpaRepository<Propiedad, Long> {
    java.util.List<Propiedad> findByEliminadaFalseOrderByIdAsc();
}
