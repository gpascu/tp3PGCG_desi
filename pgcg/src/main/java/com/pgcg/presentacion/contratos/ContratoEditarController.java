package com.pgcg.presentacion.contratos;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pgcg.entidades.Contrato;
import com.pgcg.entidades.EstadoContrato;
import com.pgcg.entidades.Persona;
import com.pgcg.entidades.Propiedad;
import com.pgcg.accesoDatos.IPersonaRepo;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.servicios.ContratoService;
import com.pgcg.servicios.PropiedadService;

@Controller
@RequestMapping("/contratos")
public class ContratoEditarController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private IPersonaRepo personaRepo;

    @ModelAttribute("propiedades")
    public List<Propiedad> getPropiedades() {
        return contratoService.getAllPropiedadesDisponibles();
    }

    @ModelAttribute("inquilinos")
    public List<Persona> getInquilinos() {
        return contratoService.getAllInquilinos();
    }

    @ModelAttribute("estados")
    public EstadoContrato[] getEstados() {
        return EstadoContrato.values();
    }

    @GetMapping("/nuevo")
    public String preparaAlta(Model modelo) {
        ContratoForm form = new ContratoForm();
        form.setEstado(EstadoContrato.BORRADOR);
        modelo.addAttribute("form", form);
        return "contratoEditar";
    }

    @GetMapping("/editar/{id}")
    public String preparaModificacion(@PathVariable Long id, Model modelo) {
        Contrato c = contratoService.getById(id);
        if (c == null) {
            return "redirect:/contratos";
        }
        ContratoForm form = new ContratoForm();
        form.setId(c.getId());
        form.setFechaInicio(c.getFechaInicio());
        form.setDuracionMeses(c.getDuracionMeses());
        form.setImporteMensual(c.getImporteMensual());
        form.setDiaVencimientoMensual(c.getDiaVencimientoMensual());
        form.setDescripcion(c.getDescripcion());
        form.setEstado(c.getEstado());
        if (c.getPropiedad() != null) {
            form.setPropiedadId(c.getPropiedad().getId());
            List<Propiedad> props = contratoService.getAllPropiedadesDisponibles();
            if (props != null) {
                props = new java.util.ArrayList<>(props);
                boolean existe = false;
                for (Propiedad p : props) {
                    if (p.getId().equals(c.getPropiedad().getId())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    props.add(c.getPropiedad());
                }
                modelo.addAttribute("propiedades", props);
            }
        }
        if (c.getInquilino() != null) {
            form.setInquilinoId(c.getInquilino().getId());
        }
        modelo.addAttribute("form", form);
        return "contratoEditar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") ContratoForm formBean, BindingResult result, RedirectAttributes ra, Model modelo) {
        if (result.hasErrors()) {
            if (formBean.getId() != null) {
                Contrato c = contratoService.getById(formBean.getId());
                if (c != null && c.getPropiedad() != null) {
                    List<Propiedad> props = contratoService.getAllPropiedadesDisponibles();
                    if (props != null) {
                        props = new java.util.ArrayList<>(props);
                        boolean existe = false;
                        for (Propiedad p : props) {
                            if (p.getId().equals(c.getPropiedad().getId())) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            props.add(c.getPropiedad());
                        }
                        modelo.addAttribute("propiedades", props);
                    }
                }
            }
            return "contratoEditar";
        }
        try {
            Contrato c = aEntidad(formBean);
            if (formBean.getId() == null) {
                contratoService.guardar(c);
                ra.addFlashAttribute("ok", "Contrato registrado correctamente");
            } else {
                contratoService.actualizar(formBean.getId(), c);
                ra.addFlashAttribute("ok", "Contrato modificado correctamente");
            }
            return "redirect:/contratos";
        } catch (Excepcion e) {
            modelo.addAttribute("error", e.getMessage());
            if (formBean.getId() != null) {
                Contrato c = contratoService.getById(formBean.getId());
                if (c != null && c.getPropiedad() != null) {
                    List<Propiedad> props = contratoService.getAllPropiedadesDisponibles();
                    if (props != null) {
                        props = new java.util.ArrayList<>(props);
                        boolean existe = false;
                        for (Propiedad p : props) {
                            if (p.getId().equals(c.getPropiedad().getId())) {
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            props.add(c.getPropiedad());
                        }
                        modelo.addAttribute("propiedades", props);
                    }
                }
            }
            return "contratoEditar";
        }
    }

    private Contrato aEntidad(ContratoForm formBean) {
        Contrato c = new Contrato();
        c.setId(formBean.getId());
        c.setFechaInicio(formBean.getFechaInicio());
        c.setDuracionMeses(formBean.getDuracionMeses());
        c.setImporteMensual(formBean.getImporteMensual());
        c.setDiaVencimientoMensual(formBean.getDiaVencimientoMensual());
        c.setDescripcion(formBean.getDescripcion());
        c.setEstado(formBean.getEstado());
        c.setEliminado(false);

        if (formBean.getPropiedadId() != null) {
            c.setPropiedad(propiedadService.buscarPorId(formBean.getPropiedadId()));
        }
        if (formBean.getInquilinoId() != null) {
            c.setInquilino(personaRepo.findById(formBean.getInquilinoId()).orElse(null));
        }
        return c;
    }
}
