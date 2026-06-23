package com.pgcg.presentacion.ciudades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.Ciudad;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.CiudadService;
import com.pgcg.servicios.ProvinciaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ciudades")
public class CiudadRegistrarEditarController {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new CiudadForm());
        cargarCombos(model);
        return "ciudades/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Ciudad ciudad = ciudadService.buscarPorId(id);
        CiudadForm form = new CiudadForm();
        form.setId(ciudad.getId());
        form.setNombre(ciudad.getNombre());
        form.setProvinciaId(ciudad.getProvincia().getId());
        model.addAttribute("form", form);
        cargarCombos(model);
        return "ciudades/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") CiudadForm form, BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "ciudades/form";
        }
        try {
            ciudadService.registrar(aEntidad(form), form.getProvinciaId());
            ra.addFlashAttribute("ok", "Ciudad guardada correctamente");
            return "redirect:/ciudades";
        } catch (Excepcion e) {
            if (e.getAtributo() != null) {
                result.rejectValue(e.getAtributo(), "error.form", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            cargarCombos(model);
            return "ciudades/form";
        }
    }

    @PostMapping("/editar")
    public String editarPost(@Valid @ModelAttribute("form") CiudadForm form, BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "ciudades/form";
        }
        try {
            ciudadService.editar(aEntidad(form), form.getProvinciaId());
            ra.addFlashAttribute("ok", "Ciudad actualizada correctamente");
            return "redirect:/ciudades";
        } catch (Excepcion e) {
            if (e.getAtributo() != null) {
                result.rejectValue(e.getAtributo(), "error.form", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            cargarCombos(model);
            return "ciudades/form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ciudadService.eliminar(id);
            ra.addFlashAttribute("ok", "Ciudad eliminada correctamente");
        } catch (Excepcion e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ciudades";
    }

    private Ciudad aEntidad(CiudadForm form) {
        Ciudad ciudad = new Ciudad();
        ciudad.setId(form.getId());
        ciudad.setNombre(form.getNombre());
        return ciudad;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("provincias", provinciaService.listarActivas());
    }
}
