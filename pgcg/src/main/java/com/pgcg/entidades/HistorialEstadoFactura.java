package com.pgcg.entidades;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class HistorialEstadoFactura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
    private LocalDateTime fechaHora;
    @ManyToOne
    private Factura factura;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
