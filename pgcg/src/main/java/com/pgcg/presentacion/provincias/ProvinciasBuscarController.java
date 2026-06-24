package com.pgcg.presentacion.provincias;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.Provincia;
import com.pgcg.servicios.ProvinciaService;

@Controller
@RequestMapping("/provincias")
public class ProvinciasBuscarController {

    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping
    public String buscar(@ModelAttribute("formBusqueda") ProvinciasBuscarForm form, Model model) {
        List<Provincia> todas = provinciaService.listarActivas();
        List<Provincia> resultado = new ArrayList<Provincia>();
        for (Provincia p : todas) {
            boolean cumple = true;
            if (form.getNombre() != null && !form.getNombre().trim().isEmpty()) {
                if (!p.getNombre().toLowerCase().contains(form.getNombre().toLowerCase())) {
                    cumple = false;
                }
            }
            if (cumple) {
                resultado.add(p);
            }
        }
        model.addAttribute("lista", resultado);
        return "provincias/lista";
    }
}
