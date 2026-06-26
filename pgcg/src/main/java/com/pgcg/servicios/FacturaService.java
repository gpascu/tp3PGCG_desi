package com.pgcg.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.pgcg.entidades.*;
import com.pgcg.excepciones.Excepcion;

public interface FacturaService {
    List<Factura> buscar(Long contratoId, Long propiedadId, Long inquilinoId, EstadoFactura estado,
                         LocalDate vencimientoDesde, LocalDate vencimientoHasta);
    Factura buscarPorId(Long id);
    List<Contrato> listarContratosFacturables();
    List<Contrato> listarContratos();
    void registrar(Factura factura, Long contratoId) throws Excepcion;
    void editar(Factura factura) throws Excepcion;
    void eliminar(Long id) throws Excepcion;
    void pagar(Long id, LocalDate fechaPago, MedioPago medio, BigDecimal importePagado, BigDecimal interes) throws Excepcion;
}
