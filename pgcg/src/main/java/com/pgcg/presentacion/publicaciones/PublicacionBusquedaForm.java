package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.EstadoPublicacion;

public class PublicacionBusquedaForm {
    private Long id;
    private EstadoPublicacion estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EstadoPublicacion getEstado() { return estado; }
    public void setEstado(EstadoPublicacion estado) { this.estado = estado; }
}