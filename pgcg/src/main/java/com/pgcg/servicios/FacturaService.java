package com.pgcg.servicios;

import java.util.List;
import com.pgcg.entidades.*;
import com.pgcg.excepciones.Excepcion;

public interface FacturaService {
    List<Factura> buscar(Long contratoId, String periodoFacturado, EstadoFactura estado);
    Factura buscarPorId(Long id);
    List<Contrato> listarContratosFacturables();
    List<Contrato> listarContratos();
    void registrar(Factura factura, Long contratoId) throws Excepcion;
    void editar(Factura factura) throws Excepcion;
    void eliminar(Long id) throws Excepcion;
}
