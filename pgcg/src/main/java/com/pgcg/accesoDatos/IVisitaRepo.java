package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Visita;

@Repository
public interface IVisitaRepo extends JpaRepository<Visita, Long> {}
