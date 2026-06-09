package com.pgcg.entidades;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class HistorialEstadoPropiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad estado;
    private LocalDateTime fechaHora;
    @ManyToOne
    private Propiedad propiedad;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EstadoDisponibilidad getEstado() { return estado; }
    public void setEstado(EstadoDisponibilidad estado) { this.estado = estado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
}
