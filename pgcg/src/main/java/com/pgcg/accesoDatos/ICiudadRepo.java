package com.pgcg.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgcg.entidades.Ciudad;

@Repository
public interface ICiudadRepo extends JpaRepository<Ciudad, Long> {

    List<Ciudad> findByEliminadaFalseOrderByNombreAsc();

    boolean existsByNombreIgnoreCaseAndProvinciaIdAndEliminadaFalse(String nombre, Long provinciaId);

    long countByProvinciaIdAndEliminadaFalse(Long provinciaId);
}
