package com.pgcg.servicios;

import java.util.List;

import com.pgcg.entidades.Provincia;
import com.pgcg.excepciones.Excepcion;

public interface ProvinciaService {

    List<Provincia> listarActivas();

    Provincia buscarPorId(Long id);

    void registrar(Provincia provincia) throws Excepcion;

    void editar(Provincia provincia) throws Excepcion;

    void eliminar(Long id) throws Excepcion;
}
