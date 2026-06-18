package com.pgcg.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgcg.accesoDatos.*;
import com.pgcg.entidades.*;
import com.pgcg.excepciones.EntidadNoEncontradaException;
import com.pgcg.excepciones.Excepcion;

@Service
public class FacturaServiceImpl implements FacturaService {
    @Autowired private IFacturaRepo facturaRepo;
    @Autowired private IContratoRepo contratoRepo;
    @Autowired private IHistorialEstadoFacturaRepo historialRepo;

    public List<Factura> buscar(Long contratoId, Long propiedadId, Long inquilinoId, EstadoFactura estado,
                               LocalDate vencimientoDesde, LocalDate vencimientoHasta) {
        List<Factura> todas = facturaRepo.findByEliminadaFalseOrderByIdAsc();
        List<Factura> resultado = new ArrayList<Factura>();
        for (Factura f : todas) {
            boolean cumple = true;
            Contrato c = f.getContrato();
            if (contratoId != null && (c == null || !c.getId().equals(contratoId))) cumple = false;
            if (propiedadId != null && (c == null || c.getPropiedad() == null || !c.getPropiedad().getId().equals(propiedadId))) cumple = false;
            if (inquilinoId != null && (c == null || c.getInquilino() == null || !c.getInquilino().getId().equals(inquilinoId))) cumple = false;
            if (estado != null && f.getEstado() != estado) cumple = false;
            if (vencimientoDesde != null && (f.getFechaVencimiento() == null || f.getFechaVencimiento().isBefore(vencimientoDesde))) cumple = false;
            if (vencimientoHasta != null && (f.getFechaVencimiento() == null || f.getFechaVencimiento().isAfter(vencimientoHasta))) cumple = false;
            if (cumple) resultado.add(f);
        }
        return resultado;
    }

    public Factura buscarPorId(Long id) {
        return facturaRepo.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Factura", id));
    }

    public List<Contrato> listarContratosFacturables() {
        List<Contrato> resultado = new ArrayList<Contrato>();
        for (Contrato c : contratoRepo.findByEliminadoFalseOrderByIdAsc()) {
            if (c.getEstado() == EstadoContrato.ACTIVO) resultado.add(c);
        }
        return resultado;
    }

    public List<Contrato> listarContratos() {
        return contratoRepo.findByEliminadoFalseOrderByIdAsc();
    }

    public void registrar(Factura factura, Long contratoId) throws Excepcion {
        if (contratoId == null) throw new Excepcion("Debe seleccionar un contrato", "contratoId");
        Contrato contrato = contratoRepo.findById(contratoId).orElse(null);
        if (contrato == null || Boolean.TRUE.equals(contrato.getEliminado()) || contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new Excepcion("Solo se puede facturar a un contrato activo y no eliminado", "contratoId");
        }
        if (factura.getEstado() == null) factura.setEstado(EstadoFactura.PENDIENTE);
        validarDatosGenerales(factura);
        validarDatosPago(factura);
        factura.setContrato(contrato);
        factura.setEliminada(false);
        Factura guardada = facturaRepo.save(factura);
        guardarHistorial(guardada, guardada.getEstado());
    }

    public void editar(Factura factura) throws Excepcion {
        Factura actual = buscarPorId(factura.getId());
        if (actual.getEstado() == EstadoFactura.ANULADA) throw new Excepcion("No se puede modificar una factura anulada");
        if (actual.getEstado() == EstadoFactura.PAGADA) throw new Excepcion("No se puede modificar una factura pagada");

        EstadoFactura nuevoEstado = factura.getEstado() == null ? actual.getEstado() : factura.getEstado();
        if (!transicionValida(actual.getEstado(), nuevoEstado)) {
            throw new Excepcion("Transición de estado no permitida: " + actual.getEstado() + " → " + nuevoEstado, "estado");
        }
        factura.setEstado(nuevoEstado);
        validarDatosGenerales(factura);
        validarDatosPago(factura);

        EstadoFactura anterior = actual.getEstado();
        // El contrato asociado no se puede modificar: se conserva el original
        actual.setConceptoFacturado(factura.getConceptoFacturado());
        actual.setFechaEmision(factura.getFechaEmision());
        actual.setFechaVencimiento(factura.getFechaVencimiento());
        actual.setImporte(factura.getImporte());
        actual.setEstado(nuevoEstado);
        actual.setFechaPago(factura.getFechaPago());
        actual.setMedio(factura.getMedio());
        actual.setImportePagado(factura.getImportePagado());
        actual.setInteres(factura.getInteres());
        Factura guardada = facturaRepo.save(actual);
        if (anterior != guardada.getEstado()) guardarHistorial(guardada, guardada.getEstado());
    }

    public void eliminar(Long id) throws Excepcion {
        Factura factura = buscarPorId(id);
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new Excepcion("No se puede eliminar una factura pagada");
        }
        factura.setEliminada(true);
        facturaRepo.save(factura);
    }

    private void validarDatosGenerales(Factura f) throws Excepcion {
        if (f.getConceptoFacturado() == null || f.getConceptoFacturado().trim().isEmpty())
            throw new Excepcion("El concepto facturado es obligatorio", "conceptoFacturado");
        if (f.getFechaEmision() == null) throw new Excepcion("La fecha de emisión es obligatoria", "fechaEmision");
        if (f.getFechaVencimiento() == null) throw new Excepcion("La fecha de vencimiento es obligatoria", "fechaVencimiento");
        if (f.getFechaVencimiento().isBefore(f.getFechaEmision()))
            throw new Excepcion("La fecha de vencimiento debe ser igual o posterior a la de emisión", "fechaVencimiento");
        if (f.getImporte() == null || f.getImporte().compareTo(BigDecimal.ZERO) <= 0)
            throw new Excepcion("El importe debe ser positivo", "importe");
    }

    private void validarDatosPago(Factura f) throws Excepcion {
        if (f.getEstado() == EstadoFactura.PAGADA) {
            // Debe estar pagada con datos completos
            if (f.getFechaPago() == null) throw new Excepcion("Debe indicar la fecha de pago", "fechaPago");
            if (f.getFechaPago().isBefore(f.getFechaEmision()))
                throw new Excepcion("La fecha de pago no puede ser anterior a la de emisión", "fechaPago");
            if (f.getMedio() == null) throw new Excepcion("Debe indicar el medio de pago", "medio");
            if (f.getImportePagado() == null || f.getImportePagado().compareTo(BigDecimal.ZERO) <= 0)
                throw new Excepcion("El importe pagado debe ser positivo", "importePagado");
            if (f.getInteres() != null && f.getInteres().compareTo(BigDecimal.ZERO) < 0)
                throw new Excepcion("El interés no puede ser negativo", "interes");
        } else {
            // Si no está pagada, los datos de pago deben estar vacíos
            if (f.getFechaPago() != null || f.getMedio() != null || f.getImportePagado() != null || f.getInteres() != null) {
                throw new Excepcion("Solo una factura pagada puede tener datos de pago");
            }
        }
    }

    private boolean transicionValida(EstadoFactura desde, EstadoFactura hacia) {
        if (desde == hacia) return true;
        switch (desde) {
            case PENDIENTE: return hacia == EstadoFactura.PAGADA || hacia == EstadoFactura.VENCIDA || hacia == EstadoFactura.ANULADA;
            case VENCIDA:   return hacia == EstadoFactura.PAGADA;
            default:        return false;
        }
    }

    private void guardarHistorial(Factura factura, EstadoFactura estado) {
        HistorialEstadoFactura h = new HistorialEstadoFactura();
        h.setFactura(factura);
        h.setEstado(estado);
        h.setFechaHora(LocalDateTime.now());
        historialRepo.save(h);
    }
}
