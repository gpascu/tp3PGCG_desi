package com.pgcg.presentacion.ciudades;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.Ciudad;
import com.pgcg.servicios.CiudadService;
import com.pgcg.servicios.ProvinciaService;

@Controller
@RequestMapping("/ciudades")
public class CiudadesBuscarController {

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping
    public String buscar(@ModelAttribute("formBusqueda") CiudadesBuscarForm form, Model model) {
        List<Ciudad> todas = ciudadService.listarActivas();
        List<Ciudad> resultado = new ArrayList<Ciudad>();
        for (Ciudad c : todas) {
            boolean cumple = true;
            if (form.getNombre() != null && !form.getNombre().trim().isEmpty()) {
                if (!c.getNombre().toLowerCase().contains(form.getNombre().toLowerCase())) {
                    cumple = false;
                }
            }
            if (form.getProvinciaId() != null) {
                if (c.getProvincia() == null || !c.getProvincia().getId().equals(form.getProvinciaId())) {
                    cumple = false;
                }
            }
            if (cumple) {
                resultado.add(c);
            }
        }
        model.addAttribute("lista", resultado);
        model.addAttribute("provincias", provinciaService.listarActivas());
        return "ciudades/lista";
    }
}
