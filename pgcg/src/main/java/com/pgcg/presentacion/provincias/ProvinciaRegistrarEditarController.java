package com.pgcg.presentacion.provincias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.Provincia;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.ProvinciaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/provincias")
public class ProvinciaRegistrarEditarController {

    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new ProvinciaForm());
        return "provincias/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Provincia provincia = provinciaService.buscarPorId(id);
        ProvinciaForm form = new ProvinciaForm();
        form.setId(provincia.getId());
        form.setNombre(provincia.getNombre());
        model.addAttribute("form", form);
        return "provincias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") ProvinciaForm form, BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            return "provincias/form";
        }
        try {
            provinciaService.registrar(aEntidad(form));
            ra.addFlashAttribute("ok", "Provincia guardada correctamente");
            return "redirect:/provincias";
        } catch (Excepcion e) {
            if (e.getAtributo() != null) {
                result.rejectValue(e.getAtributo(), "error.form", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            return "provincias/form";
        }
    }

    @PostMapping("/editar")
    public String editarPost(@Valid @ModelAttribute("form") ProvinciaForm form, BindingResult result, RedirectAttributes ra, Model model) {
        if (result.hasErrors()) {
            return "provincias/form";
        }
        try {
            provinciaService.editar(aEntidad(form));
            ra.addFlashAttribute("ok", "Provincia actualizada correctamente");
            return "redirect:/provincias";
        } catch (Excepcion e) {
            if (e.getAtributo() != null) {
                result.rejectValue(e.getAtributo(), "error.form", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            return "provincias/form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            provinciaService.eliminar(id);
            ra.addFlashAttribute("ok", "Provincia eliminada correctamente");
        } catch (Excepcion e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/provincias";
    }

    private Provincia aEntidad(ProvinciaForm form) {
        Provincia provincia = new Provincia();
        provincia.setId(form.getId());
        provincia.setNombre(form.getNombre());
        return provincia;
    }
}
