package com.pgcg.presentacion.publicaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
 
import com.pgcg.entidades.Publicacion;
import com.pgcg.servicios.PublicacionService;
 
@Controller
@RequestMapping("/publicaciones")
public class PublicacionesHistorialController {
 
    @Autowired
    private PublicacionService publicacionService;
 
    @GetMapping("/{id}/historial-estados")
    public String verHistorial(@PathVariable Long id, Model model) {
        Publicacion publicacion = publicacionService.buscarPorId(id);
        model.addAttribute("publicacion", publicacion);
        model.addAttribute("historial", publicacionService.listarHistorialEstados(id));
        return "publicaciones/historial-estados";
    }
}
