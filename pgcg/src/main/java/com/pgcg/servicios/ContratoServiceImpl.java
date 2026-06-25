package com.pgcg.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgcg.entidades.Contrato;
import com.pgcg.entidades.Persona;
import com.pgcg.entidades.Propiedad;
import com.pgcg.entidades.EstadoDisponibilidad;
import com.pgcg.entidades.HistorialEstadoContrato;
import com.pgcg.excepciones.Excepcion;
import com.pgcg.entidades.EstadoContrato;
import com.pgcg.accesoDatos.IContratoRepo;
import com.pgcg.accesoDatos.IHistorialEstadoContratoRepo;
import com.pgcg.presentacion.contratos.ContratoBuscarForm;

@Service
public class ContratoServiceImpl implements ContratoService {

    @Autowired
    private IContratoRepo repo;

    @Autowired
    private IHistorialEstadoContratoRepo historialRepo;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private PublicacionService publicacionService;

    @Override
    public List<Contrato> obtenerTodos() {
        return repo.findAll();
    }

    private void validarTransicion(EstadoContrato estadoActual, EstadoContrato estadoNuevo) throws Excepcion {
        if (estadoActual == estadoNuevo) {
            return;
        }
        if (estadoActual == EstadoContrato.FINALIZADO || estadoActual == EstadoContrato.RESCINDIDO) {
            throw new Excepcion("No se puede cambiar el estado de un contrato finalizado o rescindido.");
        }
        if (estadoActual == EstadoContrato.ACTIVO && estadoNuevo == EstadoContrato.BORRADOR) {
            throw new Excepcion("Error: Imposibilidad de regresar el estado del contrato a borrador.");
        }
    }

    private void guardarHistorial(Contrato contrato, EstadoContrato estado) {
        HistorialEstadoContrato h = new HistorialEstadoContrato();
        h.setContrato(contrato);
        h.setEstado(estado);
        h.setFechaHora(LocalDateTime.now());
        historialRepo.save(h);
    }

    private boolean tieneOtroContratoActivo(Long propiedadId, Long contratoId) {
        List<Contrato> contratos = repo.findByEliminadoFalseOrderByIdAsc();
        for (Contrato c : contratos) {
            if (c.getPropiedad() != null && c.getPropiedad().getId().equals(propiedadId)
                    && c.getEstado() == EstadoContrato.ACTIVO
                    && (contratoId == null || !c.getId().equals(contratoId))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Contrato guardar(Contrato contrato) throws Excepcion {
        if (contrato.getEstado() == EstadoContrato.BORRADOR) {
            Contrato guardado = repo.save(contrato);
            guardarHistorial(guardado, EstadoContrato.BORRADOR);
            return guardado;
        } else if (contrato.getEstado() == EstadoContrato.ACTIVO) {
            if (tieneOtroContratoActivo(contrato.getPropiedad().getId(), contrato.getId())) {
                throw new Excepcion(
                        "No se puede activar el contrato porque ya existe otro contrato activo para esta propiedad.");
            }
            if (contrato.getPropiedad().getEstadoDisponibilidad() == EstadoDisponibilidad.DISPONIBLE) {
                propiedadService.cambiarEstadoDesdeContrato(contrato.getPropiedad(), EstadoDisponibilidad.ALQUILADA);
                // Al activarse el contrato, se finalizan las publicaciones de esa propiedad
                publicacionService.finalizarPublicacionesDePropiedad(contrato.getPropiedad().getId());
                Contrato guardado = repo.save(contrato);
                guardarHistorial(guardado, EstadoContrato.ACTIVO);
                return guardado;
            } else {
                throw new Excepcion(
                        "No se puede guardar el contrato en estado ACTIVO. La propiedad no está disponible.");
            }
        } else {
            throw new Excepcion("No se puede guardar un nuevo contrato con estado " + contrato.getEstado() + ".");
        }
    }

    @Override
    public Contrato buscarPorId(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public boolean eliminar(Long id) throws Excepcion {
        Contrato existente = repo.findById(id).orElse(null);
        if (existente == null) {
            throw new Excepcion("Contrato no encontrado.");
        }
        if (existente.getEstado() != EstadoContrato.BORRADOR) {
            throw new Excepcion(
                    "Solo se pueden eliminar los contratos en estado 'BORRADOR'.");
        }
        existente.setEliminado(true);
        repo.save(existente);
        return true;
    }

    @Override
    public Contrato actualizar(Long id, Contrato datos) throws Excepcion {
        Contrato existente = repo.findById(id).get();
        EstadoContrato estadoActual = existente.getEstado();
        EstadoContrato estadoNuevo = datos.getEstado();

        if (estadoActual != estadoNuevo) {
            validarTransicion(estadoActual, estadoNuevo);

            if (estadoActual == EstadoContrato.BORRADOR && estadoNuevo == EstadoContrato.ACTIVO) {
                if (tieneOtroContratoActivo(datos.getPropiedad().getId(), id)) {
                    throw new Excepcion(
                            "No se puede activar el contrato porque ya existe otro contrato activo para esta propiedad.");
                }
                if (datos.getPropiedad().getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
                    throw new Excepcion("No se puede activar el contrato porque la propiedad no está disponible.");
                }
                propiedadService.cambiarEstadoDesdeContrato(datos.getPropiedad(), EstadoDisponibilidad.ALQUILADA);
                // Al activarse el contrato, se finalizan las publicaciones de esa propiedad
                publicacionService.finalizarPublicacionesDePropiedad(datos.getPropiedad().getId());
            } else if (estadoActual == EstadoContrato.ACTIVO && estadoNuevo == EstadoContrato.FINALIZADO) {
                if (datos.getPropiedad().getEstadoDisponibilidad() != EstadoDisponibilidad.ALQUILADA) {
                    throw new Excepcion("No se puede finalizar el contrato porque la propiedad no está alquilada.");
                }
                propiedadService.cambiarEstadoDesdeContrato(datos.getPropiedad(), EstadoDisponibilidad.DISPONIBLE);
            } else if (estadoActual == EstadoContrato.ACTIVO && estadoNuevo == EstadoContrato.RESCINDIDO) {
                if (datos.getPropiedad().getEstadoDisponibilidad() != EstadoDisponibilidad.ALQUILADA) {
                    throw new Excepcion("No se puede rescindir el contrato porque la propiedad no está alquilada.");
                }
                propiedadService.cambiarEstadoDesdeContrato(datos.getPropiedad(), EstadoDisponibilidad.DISPONIBLE);
            } else {
                throw new Excepcion("No se puede transicionar desde el estado " + estadoActual);
            }
        }

        existente.setFechaInicio(datos.getFechaInicio());
        existente.setDuracionMeses(datos.getDuracionMeses());
        existente.setImporteMensual(datos.getImporteMensual());
        existente.setDiaVencimientoMensual(datos.getDiaVencimientoMensual());
        existente.setDescripcion(datos.getDescripcion());
        existente.setEstado(datos.getEstado());
        existente.setEliminado(datos.getEliminado());
        existente.setPropiedad(datos.getPropiedad());
        existente.setInquilino(datos.getInquilino());

        Contrato guardado = repo.save(existente);

        if (estadoActual != estadoNuevo) {
            guardarHistorial(guardado, estadoNuevo);
        }

        return guardado;
    }

    @Override
    public List<Contrato> filter(ContratoBuscarForm filtro) {
        List<Contrato> todos = repo.findByEliminadoFalseOrderByIdAsc();
        if (filtro == null) {
            return todos;
        }
        List<Contrato> resultado = new ArrayList<>();
        for (Contrato c : todos) {
            boolean cumple = true;
            if (filtro.getPropiedadId() != null) {
                if (c.getPropiedad() == null || !c.getPropiedad().getId().equals(filtro.getPropiedadId())) {
                    cumple = false;
                }
            }
            if (filtro.getInquilinoId() != null) {
                if (c.getInquilino() == null || !c.getInquilino().getId().equals(filtro.getInquilinoId())) {
                    cumple = false;
                }
            }
            if (filtro.getEstado() != null) {
                if (c.getEstado() != filtro.getEstado()) {
                    cumple = false;
                }
            }
            if (filtro.getFechaInicio() != null) {
                if (c.getFechaInicio() == null || !c.getFechaInicio().equals(filtro.getFechaInicio())) {
                    cumple = false;
                }
            }
            if (cumple) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public Contrato getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Propiedad> getAllPropiedadesDisponibles() {
        return propiedadService.listarDisponibles();
    }

    @Override
    public List<Persona> getAllInquilinos() {
        return personaService.listarActivas();
    }
}
