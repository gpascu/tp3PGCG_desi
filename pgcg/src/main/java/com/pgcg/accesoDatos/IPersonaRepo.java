package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Persona;

@Repository
public interface IPersonaRepo extends JpaRepository<Persona, Long> {
    java.util.List<Persona> findByEliminadaFalseOrderByApellidoAscNombreAsc();
}
