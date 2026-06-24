package com.pgcg.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(max = 100, message = "El nombre de la ciudad no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Boolean eliminada;

    @NotNull(message = "La provincia es obligatoria")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Provincia provincia;

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

    public Boolean getEliminada() {
        return eliminada;
    }

    public void setEliminada(Boolean eliminada) {
        this.eliminada = eliminada;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
