package com.pgcg.presentacion.publicaciones;

import com.pgcg.servicios.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionesBuscarController {

    @Autowired
    private PublicacionService publicacionService;

    // Muestra la pantalla con la tabla de publicaciones
    @GetMapping("/buscar")
    public String verListado(Model model) {
        // Enviamos la lista de publicaciones no eliminadas a la pantalla
        model.addAttribute("listaPublicaciones", publicacionService.listarTodas());
        return "publicaciones/buscar"; // Ruta al archivo HTML
    }

    // Procesa la solicitud de baja lógica
    @GetMapping("/eliminar/{id}")
    public String darDeBaja(@PathVariable("id") Long id, Model model) {
        try {
            publicacionService.eliminar(id);
            return "redirect:/publicaciones/buscar"; // Redirige y refresca la tabla
        } catch (RuntimeException e) {
            // Si no se pudo eliminar (ej. no estaba ACTIVA), vuelve a la pantalla con el error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("listaPublicaciones", publicacionService.listarTodas());
            return "publicaciones/buscar";
        }
    }
}