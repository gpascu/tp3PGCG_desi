package com.pgcg.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaInicio;
    private Integer duracionMeses;
    private BigDecimal importeMensual;
    private Integer diaVencimientoMensual;
    @Column(length = 1000)
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private EstadoContrato estado;
    private Boolean eliminado;
    @ManyToOne
    private Propiedad propiedad;
    @ManyToOne
    private Persona inquilino;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public Integer getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }
    public java.math.BigDecimal getImporteMensual() { return importeMensual; }
    public void setImporteMensual(java.math.BigDecimal importeMensual) { this.importeMensual = importeMensual; }
    public Integer getDiaVencimientoMensual() { return diaVencimientoMensual; }
    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) { this.diaVencimientoMensual = diaVencimientoMensual; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }
    public Boolean getEliminado() { return eliminado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
    public Persona getInquilino() { return inquilino; }
    public void setInquilino(Persona inquilino) { this.inquilino = inquilino; }
}
