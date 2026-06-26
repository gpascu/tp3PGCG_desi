package com.pgcg.presentacion.publicaciones;

import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.servicios.PublicacionServiceImpl;
import com.pgcg.servicios.PropiedadService;
import com.pgcg.servicios.CiudadService;
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
public class PublicacionesBuscarController {

    @Autowired
    private PublicacionServiceImpl publicacionService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public String verListadoConFiltros(@ModelAttribute("formBusqueda") PublicacionBusquedaForm formBusqueda, Model model) {
        try {
            model.addAttribute("lista", publicacionService.buscarConFiltros(
                    formBusqueda.getPropiedadId(), formBusqueda.getCiudadId(), formBusqueda.getEstado(),
                    formBusqueda.getPrecioMin(), formBusqueda.getPrecioMax()));
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error al procesar la búsqueda: " + e.getMessage());
        }
        cargarCombos(model);
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
            model.addAttribute("formBusqueda", new PublicacionBusquedaForm());
            cargarCombos(model);
            return "publicaciones/buscar";
        }
    }

    private void cargarCombos(Model model) {
        model.addAttribute("estados", EstadoPublicacion.values());
        model.addAttribute("propiedades", propiedadService.buscar(null, null, null, null));
        model.addAttribute("ciudades", ciudadService.listarActivas());
    }
}
