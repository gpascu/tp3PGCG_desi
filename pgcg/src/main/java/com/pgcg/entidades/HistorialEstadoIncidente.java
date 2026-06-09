package com.pgcg.entidades;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class HistorialEstadoIncidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EstadoIncidente estado;
    private LocalDateTime fechaHora;
    @ManyToOne
    private Incidente incidente;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EstadoIncidente getEstado() { return estado; }
    public void setEstado(EstadoIncidente estado) { this.estado = estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Incidente getIncidente() { return incidente; }
    public void setIncidente(Incidente incidente) { this.incidente = incidente; }
}
