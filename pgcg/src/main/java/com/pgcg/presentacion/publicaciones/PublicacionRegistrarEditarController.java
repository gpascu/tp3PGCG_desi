package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.Publicacion;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Propiedad; 
import com.pgcg.servicios.PublicacionService;
import com.pgcg.servicios.PropiedadServiceImpl; 
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

    @Autowired
    private PropiedadServiceImpl propiedadService; 

    // 1. Muestra el formulario vacío
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("publicacionesform", new Publicacion()); 
        model.addAttribute("estados", EstadoPublicacion.values());
        
        // 🌟 AGREGADO: Buscamos todas las propiedades disponibles y se las mandamos al HTML
        // Ajusta el método 'listarTodas()' según cómo se llame en tu PropiedadService (ej. findAll(), listar())
        model.addAttribute("propiedadesDisponibles", propiedadService.listarDisponibles());
        
        return "publicaciones/publicacionesform"; 
    }

    @PostMapping("/guardar")
    public String guardarPublicacion(@ModelAttribute("publicacionesform") Publicacion publicacion, Model model) {
        try {
            publicacionService.guardar(publicacion);
            return "redirect:/publicaciones"; 
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("publicacionesform", publicacion); 
            model.addAttribute("estados", EstadoPublicacion.values());
            
            model.addAttribute("propiedadesDisponibles", propiedadService.listarDisponibles());
            
            return "publicaciones/publicacionesform"; 
        }
    }

    // Atrapa los GET accidentales
    @GetMapping("/guardar")
    public String redirigirPorGetAccidental() {
        return "redirect:/publicaciones/nuevo";
    }
}