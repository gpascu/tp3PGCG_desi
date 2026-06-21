package com.pgcg.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "El DNI/CUIT es obligatorio")
    @Size(max = 20, message = "El DNI/CUIT no puede superar los 20 caracteres")
    @Column(nullable = false, length = 20)
    private String dniCuit;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 50, message = "El teléfono no puede superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank(message = "El domicilio es obligatorio")
    @Size(max = 200, message = "El domicilio no puede superar los 200 caracteres")
    @Column(nullable = false, length = 200)
    private String domicilio;

    @Column(nullable = false)
    private Boolean eliminada;

    @NotNull(message = "La ciudad es obligatoria")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Ciudad ciudad;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDniCuit() { return dniCuit; }
    public void setDniCuit(String dniCuit) { this.dniCuit = dniCuit; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }
    public Boolean getEliminada() { return eliminada; }
    public void setEliminada(Boolean eliminada) { this.eliminada = eliminada; }
    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }

    @Transient
    public String getNombreCompleto() { return nombre + " " + apellido; }
}
