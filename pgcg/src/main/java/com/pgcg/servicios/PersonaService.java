package com.pgcg.servicios;

import java.util.List;

import com.pgcg.entidades.Persona;
import com.pgcg.excepciones.Excepcion;

public interface PersonaService {

    List<Persona> listarActivas();

    Persona buscarPorId(Long id);

    void registrar(Persona persona, Long ciudadId) throws Excepcion;

    void editar(Persona persona, Long ciudadId) throws Excepcion;

    void eliminar(Long id) throws Excepcion;
}
