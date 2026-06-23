package com.pgcg.servicios;

import java.util.List;

import com.pgcg.entidades.Ciudad;
import com.pgcg.excepciones.Excepcion;

public interface CiudadService {

    List<Ciudad> listarActivas();

    Ciudad buscarPorId(Long id);

    void registrar(Ciudad ciudad, Long provinciaId) throws Excepcion;

    void editar(Ciudad ciudad, Long provinciaId) throws Excepcion;

    void eliminar(Long id) throws Excepcion;
}

