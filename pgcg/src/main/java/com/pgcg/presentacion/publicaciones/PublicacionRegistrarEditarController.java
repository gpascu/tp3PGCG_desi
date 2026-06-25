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

    // mostrar formulario 
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("publicacionesform", new Publicacion()); 
        model.addAttribute("estados", EstadoPublicacion.values());
        
        // listar propiedades
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
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        try {
            Publicacion publicacion = publicacionService.buscarPorId(id);
            
            if (publicacion == null) {
                throw new RuntimeException("Error: Publicación no encontrada");
            }
            
            model.addAttribute("publicacionesform", publicacion);
            
            model.addAttribute("propiedades", propiedadService.listarDisponibles()); 
            model.addAttribute("estados", EstadoPublicacion.values()); 
            
            return "publicaciones/publicacionesform"; 
         } catch (RuntimeException e) {
            // 🌟 AGREGA ESTA LÍNEA AHORA: Te va a confesar el error real en la consola de Eclipse
            e.printStackTrace(); 
            
            model.addAttribute("error", e.getMessage());
            return "redirect:/publicaciones";
        }
    }

    @PostMapping("/editar/{id}")
    public String procesarEditar(@PathVariable("id") Long id, @ModelAttribute("publicacionesform") Publicacion publicacion, Model model) {
        try {
            publicacionService.modificar(id, publicacion);
            return "redirect:/publicaciones?ok=Publicación modificada con éxito";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("propiedades", propiedadService.listarDisponibles());
            model.addAttribute("estados", EstadoPublicacion.values());
            model.addAttribute("publicacionesform", publicacion);
            return "publicaciones/publicacionesform"; 
        }
    }
}