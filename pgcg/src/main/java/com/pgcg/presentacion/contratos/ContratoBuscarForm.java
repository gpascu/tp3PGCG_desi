package com.pgcg.presentacion.contratos;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.pgcg.entidades.EstadoContrato;

public class ContratoBuscarForm {
    private Long propiedadId;
    private Long inquilinoId;
    private EstadoContrato estado;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaInicio;

    public Long getPropiedadId() {
        return propiedadId;
    }

    public void setPropiedadId(Long propiedadId) {
        this.propiedadId = propiedadId;
    }

    public Long getInquilinoId() {
        return inquilinoId;
    }

    public void setInquilinoId(Long inquilinoId) {
        this.inquilinoId = inquilinoId;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
