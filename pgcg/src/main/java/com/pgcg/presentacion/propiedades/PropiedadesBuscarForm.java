package com.pgcg.presentacion.propiedades;

import com.pgcg.entidades.*;

public class PropiedadesBuscarForm {
    private String direccion;
    private Long ciudadId;
    private TipoPropiedad tipo;
    private EstadoDisponibilidad estadoDisponibilidad;
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Long getCiudadId() { return ciudadId; }
    public void setCiudadId(Long ciudadId) { this.ciudadId = ciudadId; }
    public TipoPropiedad getTipo() { return tipo; }
    public void setTipo(TipoPropiedad tipo) { this.tipo = tipo; }
    public EstadoDisponibilidad getEstadoDisponibilidad() { return estadoDisponibilidad; }
    public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) { this.estadoDisponibilidad = estadoDisponibilidad; }
}
