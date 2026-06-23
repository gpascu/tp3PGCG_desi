package com.pgcg.presentacion.propiedades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.*;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.*;

@Controller
@RequestMapping("/propiedades")
public class PropiedadRegistrarEditarController {
    @Autowired private PropiedadService propiedadService;
    @Autowired private PersonaService personaService;
    @Autowired private CiudadService ciudadService;

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        PropiedadForm form = new PropiedadForm();
        form.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        model.addAttribute("form", form);
        cargarCombos(model);
        return "propiedades/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Propiedad p = propiedadService.buscarPorId(id);
        PropiedadForm form = new PropiedadForm();
        form.setId(p.getId());
        form.setDireccion(p.getDireccion());
        form.setTipo(p.getTipo());
        form.setCantidadAmbientes(p.getCantidadAmbientes());
        form.setMetrosCuadrados(p.getMetrosCuadrados());
        form.setDescripcion(p.getDescripcion());
        form.setComodidades(p.getComodidades());
        form.setEstadoDisponibilidad(p.getEstadoDisponibilidad());
        form.setPropietarioId(p.getPropietario().getId());
        form.setCiudadId(p.getCiudad().getId());
        model.addAttribute("form", form);
        cargarCombos(model);
        return "propiedades/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("form") PropiedadForm form, RedirectAttributes ra, Model model) {
        try {
            propiedadService.registrar(aEntidad(form), form.getPropietarioId(), form.getCiudadId());
            ra.addFlashAttribute("ok", "Propiedad guardada correctamente");
            return "redirect:/propiedades";
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            cargarCombos(model);
            return "propiedades/form";
        }
    }

    @PostMapping("/editar")
    public String editarPost(@ModelAttribute("form") PropiedadForm form, RedirectAttributes ra, Model model) {
        try {
            propiedadService.editar(aEntidad(form), form.getPropietarioId(), form.getCiudadId());
            ra.addFlashAttribute("ok", "Propiedad actualizada correctamente");
            return "redirect:/propiedades";
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            cargarCombos(model);
            return "propiedades/form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            propiedadService.eliminar(id);
            ra.addFlashAttribute("ok", "Propiedad eliminada correctamente");
        } catch (Excepcion e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/propiedades";
    }

    private Propiedad aEntidad(PropiedadForm form) {
        Propiedad p = new Propiedad();
        p.setId(form.getId());
        p.setDireccion(form.getDireccion());
        p.setTipo(form.getTipo());
        p.setCantidadAmbientes(form.getCantidadAmbientes());
        p.setMetrosCuadrados(form.getMetrosCuadrados());
        p.setDescripcion(form.getDescripcion());
        p.setComodidades(form.getComodidades());
        p.setEstadoDisponibilidad(form.getEstadoDisponibilidad());
        return p;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("personas", personaService.listarActivas());
        model.addAttribute("ciudades", ciudadService.listarActivas());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoDisponibilidad.values());
    }
}
