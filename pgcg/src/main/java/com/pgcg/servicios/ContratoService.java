package com.pgcg.servicios;

import java.util.List;

import com.pgcg.entidades.Contrato;
import com.pgcg.entidades.Persona;
import com.pgcg.entidades.Propiedad;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.presentacion.contratos.ContratoBuscarForm;

public interface ContratoService {
    List<Contrato> obtenerTodos();

    Contrato guardar(Contrato contrato) throws Excepcion;

    Contrato buscarPorId(Long id);

    boolean eliminar(Long id) throws Excepcion;

    Contrato actualizar(Long id, Contrato datos) throws Excepcion;

    List<Contrato> filter(ContratoBuscarForm filtro);

    Contrato getById(Long id);

    List<Propiedad> getAllPropiedadesDisponibles();

    List<Persona> getAllInquilinos();
}
