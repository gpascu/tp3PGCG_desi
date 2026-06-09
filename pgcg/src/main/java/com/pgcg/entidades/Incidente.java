package com.pgcg.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(length = 1000)
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private CategoriaIncidente categoria;
    private LocalDateTime fechaAlta;
    @Enumerated(EnumType.STRING)
    private PrioridadIncidente prioridad;
    @Enumerated(EnumType.STRING)
    private EstadoIncidente estado;
    private Boolean eliminado;
    private LocalDateTime fechaResolucion;
    @Column(length = 1000)
    private String observacionesResolucion;
    private BigDecimal costoResolucion;
    private String responsableTecnico;
    @ManyToOne
    private Contrato contrato;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public CategoriaIncidente getCategoria() { return categoria; }
    public void setCategoria(CategoriaIncidente categoria) { this.categoria = categoria; }
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
    public PrioridadIncidente getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadIncidente prioridad) { this.prioridad = prioridad; }
    public EstadoIncidente getEstado() { return estado; }
    public void setEstado(EstadoIncidente estado) { this.estado = estado; }
    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }
    public String getObservacionesResolucion() { return observacionesResolucion; }
    public void setObservacionesResolucion(String observacionesResolucion) { this.observacionesResolucion = observacionesResolucion; }
    public BigDecimal getCostoResolucion() { return costoResolucion; }
    public void setCostoResolucion(BigDecimal costoResolucion) { this.costoResolucion = costoResolucion; }
    public String getResponsableTecnico() { return responsableTecnico; }
    public void setResponsableTecnico(String responsableTecnico) { this.responsableTecnico = responsableTecnico; }
    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
}
