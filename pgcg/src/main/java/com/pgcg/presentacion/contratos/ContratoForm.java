package com.pgcg.presentacion.contratos;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.pgcg.entidades.EstadoContrato;

public class ContratoForm {
    private Long id;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @NotNull(message = "La duración en meses es obligatoria")
    @Min(value = 1, message = "La duración en meses debe ser un número positivo")
    private Integer duracionMeses;

    @NotNull(message = "El importe mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe mensual debe ser un número positivo")
    private BigDecimal importeMensual;

    @NotNull(message = "El día de vencimiento mensual es obligatorio")
    @Min(value = 1, message = "El día de vencimiento debe ser entre 1 y 31")
    @Max(value = 31, message = "El día de vencimiento debe ser entre 1 y 31")
    private Integer diaVencimientoMensual;

    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private EstadoContrato estado;

    @NotNull(message = "Debe seleccionar una propiedad")
    private Long propiedadId;

    @NotNull(message = "Debe seleccionar un inquilino")
    private Long inquilinoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public BigDecimal getImporteMensual() {
        return importeMensual;
    }

    public void setImporteMensual(BigDecimal importeMensual) {
        this.importeMensual = importeMensual;
    }

    public Integer getDiaVencimientoMensual() {
        return diaVencimientoMensual;
    }

    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
        this.diaVencimientoMensual = diaVencimientoMensual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

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
}
