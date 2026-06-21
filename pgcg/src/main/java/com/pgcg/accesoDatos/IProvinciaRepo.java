package com.pgcg.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgcg.entidades.Provincia;

@Repository
public interface IProvinciaRepo extends JpaRepository<Provincia, Long> {

    List<Provincia> findByEliminadaFalseOrderByNombreAsc();

    boolean existsByNombreIgnoreCaseAndEliminadaFalse(String nombre);
}
