package com.pgcg.presentacion.contratos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pgcg.entidades.Contrato;
import com.pgcg.servicios.ContratoService;

@Controller
@RequestMapping("/contratos")
public class ContratoHistorialController {

    @Autowired
    private ContratoService contratoService;

    @GetMapping("/{id}/historial-estados")
    public String verHistorial(@PathVariable Long id, Model model) {
        Contrato contrato = contratoService.buscarPorId(id);
        model.addAttribute("contrato", contrato);
        model.addAttribute("historial", contratoService.listarHistorialEstados(id));
        return "contratoHistorialEstados";
    }
}
