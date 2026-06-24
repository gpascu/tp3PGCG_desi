package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.Publicacion;
import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.servicios.PublicacionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionesBuscarController {

    @Autowired
    private PublicacionServiceImpl publicacionService;

    @GetMapping
    public String verListadoConFiltros(@ModelAttribute("formBusqueda") PublicacionBusquedaForm formBusqueda, Model model) {
        try {
            List<Publicacion> lista;

            // Filtrar por id o estado
            if (formBusqueda.getId() != null || formBusqueda.getEstado() != null) {
                lista = publicacionService.buscarConFiltros(formBusqueda.getId(), formBusqueda.getEstado());
            } else {
                lista = publicacionService.listarTodas();
            }

            model.addAttribute("lista", lista); // 'lista' coincide con el th:each="p : ${lista}"
            model.addAttribute("estados", EstadoPublicacion.values());
            
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error al procesar la búsqueda: " + e.getMessage());
        }
        return "publicaciones/buscar";
    }

    @PostMapping("/eliminar/{id}")
    public String darDeBaja(@PathVariable("id") Long id, Model model) {
        try {
            publicacionService.eliminar(id);
            return "redirect:/publicaciones";
        } catch (RuntimeException e) {
            model.addAttribute("error", "No se pudo dar de baja: " + e.getMessage());
            model.addAttribute("lista", publicacionService.listarTodas());
            model.addAttribute("estados", EstadoPublicacion.values());
            model.addAttribute("formBusqueda", new PublicacionBusquedaForm());
            return "publicaciones/buscar";
        }
    }
}