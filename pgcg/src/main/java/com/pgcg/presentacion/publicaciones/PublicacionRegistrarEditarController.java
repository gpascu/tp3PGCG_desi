package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.Publicacion;
import com.pgcg.servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionRegistrarEditarController {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        // "publicacionForm" es el objeto vacío que se unirá a los campos del HTML
        model.addAttribute("publicacionForm", new Publicacion()); 
        return "publicaciones/form"; // Ruta al archivo HTML
    }

    // Procesa el envío del formulario al hacer clic en "Guardar"
    @PostMapping("/guardar")
    public String guardarPublicacion(@ModelAttribute("publicacionForm") Publicacion publicacion, Model model) {
        try {
            publicacionService.guardar(publicacion);
            return "redirect:/publicaciones/buscar"; // Si todo sale bien, redirige al listado
        } catch (RuntimeException e) {
            // Si salta un "if" de validación, vuelve al formulario mostrando el mensaje de error claro
            model.addAttribute("error", e.getMessage());
            return "publicaciones/form";
        }
    }
}