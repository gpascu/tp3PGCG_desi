package com.pgcg.presentacion.personas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.Persona;
import com.pgcg.servicios.CiudadService;
import com.pgcg.servicios.PersonaService;

@Controller
@RequestMapping("/personas")
public class PersonasBuscarController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public String buscar(@ModelAttribute("formBusqueda") PersonasBuscarForm form, Model model) {
        List<Persona> todas = personaService.listarActivas();
        List<Persona> resultado = new ArrayList<Persona>();
        for (Persona p : todas) {
            boolean cumple = true;
            if (form.getNombreApellido() != null && !form.getNombreApellido().trim().isEmpty()) {
                String texto = form.getNombreApellido().toLowerCase();
                String nombre = p.getNombre() != null ? p.getNombre().toLowerCase() : "";
                String apellido = p.getApellido() != null ? p.getApellido().toLowerCase() : "";
                String completo = (nombre + " " + apellido).trim();
                if (!nombre.contains(texto) && !apellido.contains(texto) && !completo.contains(texto)) {
                    cumple = false;
                }
            }
            if (form.getCiudadId() != null) {
                if (p.getCiudad() == null || !p.getCiudad().getId().equals(form.getCiudadId())) {
                    cumple = false;
                }
            }
            if (cumple) {
                resultado.add(p);
            }
        }
        model.addAttribute("lista", resultado);
        model.addAttribute("ciudades", ciudadService.listarActivas());
        return "personas/lista";
    }
}
