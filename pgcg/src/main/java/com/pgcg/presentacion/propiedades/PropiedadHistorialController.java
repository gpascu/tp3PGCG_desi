package com.pgcg.presentacion.propiedades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.Propiedad;
import com.pgcg.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedades")
public class PropiedadHistorialController {

    @Autowired
    private PropiedadService propiedadService;

    @GetMapping("/{id}/historial-estados")
    public String verHistorial(@PathVariable Long id, Model model) {
        Propiedad propiedad = propiedadService.buscarPorId(id);
        model.addAttribute("propiedad", propiedad);
        model.addAttribute("historial", propiedadService.listarHistorialEstados(id));
        return "propiedades/historial-estados";
    }
}

//Agregado 25junio2026 para consultar historico de esa propiedad