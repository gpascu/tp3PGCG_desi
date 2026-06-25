package com.pgcg.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal precioMensual;
    @Column(length = 1000)
    private String condiciones;
    private LocalDate fechaPublicacion;
    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estado;
    private Boolean eliminada;
    @Column(length = 1000)
    private String descripcion;
 // EAGER obliga a Hibernate a traer la propiedad siempre adjunta a la publicación, evitando que dé null
 @ManyToOne(fetch = FetchType.EAGER)
 @JoinColumn(name = "propiedad_id") 
 private Propiedad propiedad;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public EstadoPublicacion getEstado() { return estado; }
    public void setEstado(EstadoPublicacion estado) { this.estado = estado; }
    public Boolean getEliminada() { return eliminada; }
    public void setEliminada(Boolean eliminada) { this.eliminada = eliminada; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
	public Publicacion orElseThrow(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
