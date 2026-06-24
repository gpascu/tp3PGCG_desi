package com.pgcg.presentacion.facturas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.EstadoFactura;
import com.pgcg.servicios.FacturaService;

@Controller
@RequestMapping("/facturas")
public class FacturasBuscarController {
    @Autowired private FacturaService facturaService;

    @GetMapping
    public String buscar(@ModelAttribute("formBusqueda") FacturasBuscarForm form, Model model) {
        model.addAttribute("lista", facturaService.buscar(form.getContratoId(),
                form.getPeriodoFacturado(), form.getEstado()));
        cargarCombos(model);
        return "facturas/lista";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("contratos", facturaService.listarContratos());
        model.addAttribute("estados", EstadoFactura.values());
    }
}
