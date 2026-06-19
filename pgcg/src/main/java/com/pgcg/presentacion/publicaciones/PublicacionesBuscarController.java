package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.Publicacion;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/publicaciones") 
public class PublicacionesBuscarController {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping
    public String verListadoConFiltros(Model model) {
        try {
            // Traemos la lista completa para mostrar inicialmente
            List<Publicacion> lista = publicacionService.listarTodas();
            model.addAttribute("listaPublicaciones", lista);
            
            // Enviamos los estados para que puedas usarlos en un filtro desplegable si querés
            model.addAttribute("estados", EstadoPublicacion.values());
            
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error al cargar el buscador: " + e.getMessage());
        }
        
        // Retorna buscar.html que ahora tendrá los filtros, la lista y el botón alta
        return "publicaciones/buscar";
    }

    // Acción de eliminación (Baja lógica)
    @GetMapping("/eliminar/{id}")
    public String darDeBaja(@PathVariable("id") Long id, Model model) {
        try {
            publicacionService.eliminar(id);
            // 🌟 Al terminar, redirige a la raíz del módulo
            return "redirect:/publicaciones";
        } catch (RuntimeException e) {
            model.addAttribute("error", "No se pudo dar de baja: " + e.getMessage());
            model.addAttribute("listaPublicaciones", publicacionService.listarTodas());
            model.addAttribute("estados", EstadoPublicacion.values());
            return "publicaciones/buscar";
        }
    }
}