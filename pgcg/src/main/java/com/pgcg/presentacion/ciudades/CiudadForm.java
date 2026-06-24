package com.pgcg.presentacion.ciudades;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CiudadForm {

    private Long id;

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(max = 100, message = "El nombre de la ciudad no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "Debe seleccionar una provincia")
    private Long provinciaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }
}
