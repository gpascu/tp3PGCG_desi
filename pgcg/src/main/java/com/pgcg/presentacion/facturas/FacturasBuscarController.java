package com.pgcg.presentacion.facturas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.EstadoFactura;
import com.pgcg.servicios.FacturaService;
import com.pgcg.servicios.PersonaService;
import com.pgcg.servicios.PropiedadService;

@Controller
@RequestMapping("/facturas")
public class FacturasBuscarController {
    @Autowired private FacturaService facturaService;
    @Autowired private PersonaService personaService;
    @Autowired private PropiedadService propiedadService;

    @GetMapping
    public String buscar(@ModelAttribute("formBusqueda") FacturasBuscarForm form, Model model) {
        model.addAttribute("lista", facturaService.buscar(form.getContratoId(), form.getPropiedadId(),
                form.getInquilinoId(), form.getEstado(), form.getVencimientoDesde(), form.getVencimientoHasta()));
        cargarCombos(model);
        return "facturas/lista";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("contratos", facturaService.listarContratos());
        model.addAttribute("propiedades", propiedadService.buscar(null, null, null, null));
        model.addAttribute("inquilinos", personaService.listarActivas());
        model.addAttribute("estados", EstadoFactura.values());
    }
}
