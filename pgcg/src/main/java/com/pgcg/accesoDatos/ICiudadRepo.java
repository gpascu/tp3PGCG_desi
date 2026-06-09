package com.pgcg.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pgcg.entidades.Ciudad;

@Repository
public interface ICiudadRepo extends JpaRepository<Ciudad, Long> {}
