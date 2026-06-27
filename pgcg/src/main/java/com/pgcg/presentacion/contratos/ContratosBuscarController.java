package com.pgcg.presentacion.contratos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.EstadoContrato;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.ContratoService;
import com.pgcg.servicios.PropiedadService;

@Controller
@RequestMapping("/contratos")
public class ContratosBuscarController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping
    public String preparaConsulta(Model modelo) {
        modelo.addAttribute("formBusqueda", new ContratoBuscarForm());
        modelo.addAttribute("lista", contratoService.filter(new ContratoBuscarForm()));
        cargarCombos(modelo);
        return "contratos/contratosBuscar";
    }

    @PostMapping
    public String buscar(@ModelAttribute("formBusqueda") ContratoBuscarForm formBean, Model modelo) {
        modelo.addAttribute("lista", contratoService.filter(formBean));
        cargarCombos(modelo);
        return "contratos/contratosBuscar";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            contratoService.eliminar(id);
            ra.addFlashAttribute("ok", "Contrato eliminado correctamente");
        } catch (Excepcion e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contratos";
    }

    private void cargarCombos(Model modelo) {
        modelo.addAttribute("propiedades", propiedadService.buscar(null, null, null, null));
        modelo.addAttribute("inquilinos", contratoService.getAllInquilinos());
        modelo.addAttribute("estados", EstadoContrato.values());
    }
}
