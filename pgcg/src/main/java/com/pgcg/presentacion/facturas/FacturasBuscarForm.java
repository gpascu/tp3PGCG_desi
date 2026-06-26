package com.pgcg.presentacion.facturas;

import java.time.LocalDate;
import com.pgcg.entidades.*;

public class FacturasBuscarForm {
    private Long contratoId;
    private Long propiedadId;
    private Long inquilinoId;
    private EstadoFactura estado;
    private LocalDate vencimientoDesde;
    private LocalDate vencimientoHasta;

    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }
    public Long getPropiedadId() { return propiedadId; }
    public void setPropiedadId(Long propiedadId) { this.propiedadId = propiedadId; }
    public Long getInquilinoId() { return inquilinoId; }
    public void setInquilinoId(Long inquilinoId) { this.inquilinoId = inquilinoId; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDate getVencimientoDesde() { return vencimientoDesde; }
    public void setVencimientoDesde(LocalDate vencimientoDesde) { this.vencimientoDesde = vencimientoDesde; }
    public LocalDate getVencimientoHasta() { return vencimientoHasta; }
    public void setVencimientoHasta(LocalDate vencimientoHasta) { this.vencimientoHasta = vencimientoHasta; }
}
