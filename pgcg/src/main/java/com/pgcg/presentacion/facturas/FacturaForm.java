package com.pgcg.presentacion.facturas;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.pgcg.entidades.*;

public class FacturaForm {
    private Long id;
    private Long contratoId;
    private String conceptoFacturado;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEmision;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;
    private BigDecimal importe;
    private EstadoFactura estado;
    // datos de pago (opcionales)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;
    private MedioPago medio;
    private BigDecimal importePagado;
    private BigDecimal interes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }
    public String getConceptoFacturado() { return conceptoFacturado; }
    public void setConceptoFacturado(String conceptoFacturado) { this.conceptoFacturado = conceptoFacturado; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public MedioPago getMedio() { return medio; }
    public void setMedio(MedioPago medio) { this.medio = medio; }
    public BigDecimal getImportePagado() { return importePagado; }
    public void setImportePagado(BigDecimal importePagado) { this.importePagado = importePagado; }
    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }
}
