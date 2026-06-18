package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.Publicacion;
import com.pgcg.entidades.EstadoPublicacion; 
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
        model.addAttribute("publicacionesform", new Publicacion()); 
        model.addAttribute("estados", EstadoPublicacion.values());
        
        return "publicaciones/publicacionesform"; 
    }

    @PostMapping("/guardar")
    public String guardarPublicacion(@ModelAttribute("publicacionesform") Publicacion publicacion, Model model) {
        try {
            publicacionService.guardar(publicacion);
            return "redirect:/publicaciones/buscar"; 
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("publicacionesform", publicacion); 
            model.addAttribute("estados", EstadoPublicacion.values());
            
            return "publicaciones/publicacionesform"; 
        }
    }

    @GetMapping("/guardar")
    public String redirigirPorGetAccidental() {
        return "redirect:/publicaciones/nuevo";
    }
}