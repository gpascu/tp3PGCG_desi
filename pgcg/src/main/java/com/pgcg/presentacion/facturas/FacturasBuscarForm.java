package com.pgcg.presentacion.facturas;

import com.pgcg.entidades.*;

public class FacturasBuscarForm {
    private Long contratoId;
    private String periodoFacturado;
    private EstadoFactura estado;

    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }
    public String getPeriodoFacturado() { return periodoFacturado; }
    public void setPeriodoFacturado(String periodoFacturado) { this.periodoFacturado = periodoFacturado; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
}
