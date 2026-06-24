package com.pgcg.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgcg.entidades.Persona;

@Repository
public interface IPersonaRepo extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadaFalseOrderByApellidoAscNombreAsc();

    boolean existsByDniCuitAndEliminadaFalse(String dniCuit);

    long countByCiudadIdAndEliminadaFalse(Long ciudadId);
}
