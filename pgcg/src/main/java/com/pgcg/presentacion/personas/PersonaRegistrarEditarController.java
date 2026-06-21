package com.pgcg.presentacion.personas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.Persona;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.CiudadService;
import com.pgcg.servicios.PersonaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/personas")
public class PersonaRegistrarEditarController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new PersonaForm());
        cargarCombos(model);
        return "personas/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Persona persona = personaService.buscarPorId(id);
        PersonaForm form = new PersonaForm();
        form.setId(persona.getId());
        form.setNombre(persona.getNombre());
        form.setApellido(persona.getApellido());
        form.setDniCuit(persona.getDniCuit());
        form.setTelefono(persona.getTelefono());
        form.setEmail(persona.getEmail());
        form.setDomicilio(persona.getDomicilio());
        form.setCiudadId(persona.getCiudad().getId());
        model.addAttribute("form", form);
        cargarCombos(model);
        return "personas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") PersonaForm form, BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "personas/form";
        }
        try {
            personaService.registrar(aEntidad(form), form.getCiudadId());
            ra.addFlashAttribute("ok", "Persona guardada correctamente");
            return "redirect:/personas";
        } catch (Excepcion e) {
            if (e.getAtributo() != null) {
                result.rejectValue(e.getAtributo(), "error.form", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            cargarCombos(model);
            return "personas/form";
        }
    }

    @PostMapping("/editar")
    public String editarPost(@Valid @ModelAttribute("form") PersonaForm form, BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "personas/form";
        }
        try {
            personaService.editar(aEntidad(form), form.getCiudadId());
            ra.addFlashAttribute("ok", "Persona actualizada correctamente");
            return "redirect:/personas";
        } catch (Excepcion e) {
            if (e.getAtributo() != null) {
                result.rejectValue(e.getAtributo(), "error.form", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            cargarCombos(model);
            return "personas/form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            personaService.eliminar(id);
            ra.addFlashAttribute("ok", "Persona eliminada correctamente");
        } catch (Excepcion e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/personas";
    }

    private Persona aEntidad(PersonaForm form) {
        Persona persona = new Persona();
        persona.setId(form.getId());
        persona.setNombre(form.getNombre());
        persona.setApellido(form.getApellido());
        persona.setDniCuit(form.getDniCuit());
        persona.setTelefono(form.getTelefono());
        persona.setEmail(form.getEmail());
        persona.setDomicilio(form.getDomicilio());
        return persona;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("ciudades", ciudadService.listarActivas());
    }
}
