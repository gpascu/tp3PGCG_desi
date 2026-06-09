package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Incidente;

@Repository
public interface IIncidenteRepo extends JpaRepository<Incidente, Long> {}
