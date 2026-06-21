package com.pgcg.presentacion.provincias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProvinciaForm {

    private Long id;

    @NotBlank(message = "El nombre de la provincia es obligatorio")
    @Size(max = 100, message = "El nombre de la provincia no puede superar los 100 caracteres")
    private String nombre;

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
}
