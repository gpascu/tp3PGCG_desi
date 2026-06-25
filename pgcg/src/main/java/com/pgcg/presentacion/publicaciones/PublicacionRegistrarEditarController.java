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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionRegistrarEditarController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PropiedadServiceImpl propiedadService; 

    // 1. Muestra el formulario vacío (alta)
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        Publicacion publicacion = new Publicacion();
        // Propiedad inicializada para que el binding de *{propiedad.id} funcione
        publicacion.setPropiedad(new Propiedad());
        model.addAttribute("publicacionesform", publicacion);
        model.addAttribute("estados", EstadoPublicacion.values());
        model.addAttribute("propiedadesDisponibles", propiedadService.listarDisponibles());

        return "publicaciones/publicacionesform";
    }

    // 1b. Muestra el formulario con una publicación existente (edición)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Publicacion publicacion = publicacionService.buscarPorId(id);
        if (publicacion == null) {
            return "redirect:/publicaciones";
        }
        model.addAttribute("publicacionesform", publicacion);
        model.addAttribute("estados", EstadoPublicacion.values());
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