package com.pgcg.servicios;

import java.util.List;
import com.pgcg.entidades.*;
import com.pgcg.excepciones.Excepcion;

public interface PropiedadService {
    List<Propiedad> buscar(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoDisponibilidad estado);
    List<Propiedad> listarDisponibles();
    List<HistorialEstadoPropiedad> listarHistorialEstados(Long propiedadId);//Agregado para historico
    Propiedad buscarPorId(Long id);
    void registrar(Propiedad propiedad, Long propietarioId, Long ciudadId) throws Excepcion;
    void editar(Propiedad propiedad, Long propietarioId, Long ciudadId) throws Excepcion;
    void eliminar(Long id) throws Excepcion;
    void cambiarEstadoDesdeContrato(Propiedad propiedad, EstadoDisponibilidad estado);
}
