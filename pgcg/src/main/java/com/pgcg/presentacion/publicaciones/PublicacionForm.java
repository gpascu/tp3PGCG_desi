package com.pgcg.presentacion.publicaciones;

import java.math.BigDecimal;

public class PublicacionForm {

    // campos 
    private Long id;
    private String condiciones;
    private String descripcion;
    private BigDecimal precioMensual;
    private Long propiedadId; // Guardamos solo el ID de la propiedad elegida en el formulario

 
    public PublicacionForm() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecioMensual() {
		return precioMensual;
	}

	public void setPrecioMensual(BigDecimal precioMensual) {
		this.precioMensual = precioMensual;
	}

	public Long getPropiedadId() {
		return propiedadId;
	}

	public void setPropiedadId(Long propiedadId) {
		this.propiedadId = propiedadId;
	}


}