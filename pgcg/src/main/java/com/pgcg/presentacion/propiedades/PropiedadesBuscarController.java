package com.pgcg.presentacion.propiedades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.EstadoDisponibilidad;
import com.pgcg.entidades.TipoPropiedad;
import com.pgcg.servicios.CiudadService;
import com.pgcg.servicios.PropiedadService;

@Controller
@RequestMapping("/propiedades")
public class PropiedadesBuscarController {
    @Autowired private PropiedadService propiedadService;
    @Autowired private CiudadService ciudadService;

    @GetMapping
    public String buscar(@ModelAttribute("formBusqueda") PropiedadesBuscarForm form, Model model) {
        model.addAttribute("lista", propiedadService.buscar(form.getDireccion(), form.getCiudadId(), form.getTipo(), form.getEstadoDisponibilidad()));
        model.addAttribute("ciudades", ciudadService.listarTodas());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoDisponibilidad.values());
        return "propiedades/lista";
    }
}
