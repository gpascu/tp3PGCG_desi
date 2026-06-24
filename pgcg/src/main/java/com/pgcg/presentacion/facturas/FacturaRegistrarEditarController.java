package com.pgcg.presentacion.facturas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.*;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.FacturaService;

@Controller
@RequestMapping("/facturas")
public class FacturaRegistrarEditarController {
    @Autowired private FacturaService facturaService;

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        FacturaForm form = new FacturaForm();
        form.setEstado(EstadoFactura.PENDIENTE);
        model.addAttribute("form", form);
        cargarCombos(model);
        return "facturas/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Factura f = facturaService.buscarPorId(id);
        FacturaForm form = new FacturaForm();
        form.setId(f.getId());
        form.setContratoId(f.getContrato() != null ? f.getContrato().getId() : null);
        form.setPeriodoFacturado(f.getPeriodoFacturado());
        form.setFechaEmision(f.getFechaEmision());
        form.setFechaVencimiento(f.getFechaVencimiento());
        form.setImporte(f.getImporte());
        form.setEstado(f.getEstado());
        form.setFechaPago(f.getFechaPago());
        form.setMedio(f.getMedio());
        form.setImportePagado(f.getImportePagado());
        model.addAttribute("form", form);
        model.addAttribute("contratoActual", f.getContrato());
        cargarCombos(model);
        return "facturas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("form") FacturaForm form, RedirectAttributes ra, Model model) {
        try {
            facturaService.registrar(aEntidad(form), form.getContratoId());
            ra.addFlashAttribute("ok", "Factura guardada correctamente");
            return "redirect:/facturas";
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            cargarCombos(model);
            return "facturas/form";
        }
    }

    @PostMapping("/editar")
    public String editarPost(@ModelAttribute("form") FacturaForm form, RedirectAttributes ra, Model model) {
        try {
            facturaService.editar(aEntidad(form));
            ra.addFlashAttribute("ok", "Factura actualizada correctamente");
            return "redirect:/facturas";
        } catch (Excepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("contratoActual", facturaService.buscarPorId(form.getId()).getContrato());
            cargarCombos(model);
            return "facturas/form";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            facturaService.eliminar(id);
            ra.addFlashAttribute("ok", "Factura eliminada correctamente");
        } catch (Excepcion e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/facturas";
    }

    private Factura aEntidad(FacturaForm form) {
        Factura f = new Factura();
        f.setId(form.getId());
        f.setPeriodoFacturado(form.getPeriodoFacturado());
        f.setFechaEmision(form.getFechaEmision());
        f.setFechaVencimiento(form.getFechaVencimiento());
        f.setImporte(form.getImporte());
        f.setEstado(form.getEstado());
        f.setFechaPago(form.getFechaPago());
        f.setMedio(form.getMedio());
        f.setImportePagado(form.getImportePagado());
        return f;
    }

    private void cargarCombos(Model model) {
        model.addAttribute("contratos", facturaService.listarContratosFacturables());
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("medios", MedioPago.values());
    }
}
