package com.pgcg.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgcg.accesoDatos.*;
import com.pgcg.entidades.*;
import com.pgcg.excepciones.EntidadNoEncontradaException;
import com.pgcg.excepciones.Excepcion;

@Service
public class PropiedadServiceImpl implements PropiedadService {
    @Autowired private IPropiedadRepo propiedadRepo;
    @Autowired private IPersonaRepo personaRepo;
    @Autowired private ICiudadRepo ciudadRepo;
    @Autowired private IContratoRepo contratoRepo;
    @Autowired private IHistorialEstadoPropiedadRepo historialRepo;

    public List<Propiedad> buscar(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoDisponibilidad estado) {
        List<Propiedad> todas = propiedadRepo.findByEliminadaFalseOrderByIdAsc();
        List<Propiedad> resultado = new ArrayList<Propiedad>();
        for (Propiedad p : todas) {
            boolean cumple = true;
            if (direccion != null && !direccion.trim().isEmpty() && !p.getDireccion().toLowerCase().contains(direccion.toLowerCase())) cumple = false;
            if (ciudadId != null && (p.getCiudad() == null || !p.getCiudad().getId().equals(ciudadId))) cumple = false;
            if (tipo != null && p.getTipo() != tipo) cumple = false;
            if (estado != null && p.getEstadoDisponibilidad() != estado) cumple = false;
            if (cumple) resultado.add(p);
        }
        return resultado;
    }

    public List<Propiedad> listarDisponibles() {
        List<Propiedad> todas = propiedadRepo.findByEliminadaFalseOrderByIdAsc();
        List<Propiedad> resultado = new ArrayList<Propiedad>();
        for (Propiedad p : todas) if (p.getEstadoDisponibilidad() == EstadoDisponibilidad.DISPONIBLE) resultado.add(p);
        return resultado;
    }

    public Propiedad buscarPorId(Long id) {
        return propiedadRepo.findById(id).orElseThrow(() -> new EntidadNoEncontradaException("Propiedad", id));
    }

    public void registrar(Propiedad propiedad, Long propietarioId, Long ciudadId) throws Excepcion {
        validar(propiedad, propietarioId, ciudadId, null);
        propiedad.setPropietario(personaRepo.findById(propietarioId).orElseThrow(() -> new EntidadNoEncontradaException("Persona", propietarioId)));
        propiedad.setCiudad(ciudadRepo.findById(ciudadId).orElseThrow(() -> new EntidadNoEncontradaException("Ciudad", ciudadId)));
        if (propiedad.getEstadoDisponibilidad() == null) propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        propiedad.setEliminada(false);
        Propiedad guardada = propiedadRepo.save(propiedad);
        guardarHistorial(guardada, guardada.getEstadoDisponibilidad());
    }

    public void editar(Propiedad propiedad, Long propietarioId, Long ciudadId) throws Excepcion {
        Propiedad actual = buscarPorId(propiedad.getId());
        validar(propiedad, propietarioId, ciudadId, propiedad.getId());
        if (tieneContratoActivo(actual.getId())) {
            if (propiedad.getEstadoDisponibilidad() == EstadoDisponibilidad.DISPONIBLE || propiedad.getEstadoDisponibilidad() == EstadoDisponibilidad.INACTIVA) {
                throw new Excepcion("No se puede cambiar a disponible o inactiva si tiene un contrato activo", "estadoDisponibilidad");
            }
        }
        EstadoDisponibilidad anterior = actual.getEstadoDisponibilidad();
        actual.setDireccion(propiedad.getDireccion());
        actual.setTipo(propiedad.getTipo());
        actual.setCantidadAmbientes(propiedad.getCantidadAmbientes());
        actual.setMetrosCuadrados(propiedad.getMetrosCuadrados());
        actual.setDescripcion(propiedad.getDescripcion());
        actual.setComodidades(propiedad.getComodidades());
        actual.setEstadoDisponibilidad(propiedad.getEstadoDisponibilidad());
        actual.setPropietario(personaRepo.findById(propietarioId).orElseThrow(() -> new EntidadNoEncontradaException("Persona", propietarioId)));
        actual.setCiudad(ciudadRepo.findById(ciudadId).orElseThrow(() -> new EntidadNoEncontradaException("Ciudad", ciudadId)));
        Propiedad guardada = propiedadRepo.save(actual);
        if (anterior != guardada.getEstadoDisponibilidad()) guardarHistorial(guardada, guardada.getEstadoDisponibilidad());
    }

    public void eliminar(Long id) throws Excepcion {
        Propiedad propiedad = buscarPorId(id);
        if (tieneContratoActivo(id)) throw new Excepcion("No se puede eliminar la propiedad porque tiene un contrato activo vigente");
        propiedad.setEliminada(true);
        propiedadRepo.save(propiedad);
    }

    public void cambiarEstadoDesdeContrato(Propiedad propiedad, EstadoDisponibilidad estado) {
        propiedad.setEstadoDisponibilidad(estado);
        Propiedad guardada = propiedadRepo.save(propiedad);
        guardarHistorial(guardada, estado);
    }

    private boolean tieneContratoActivo(Long propiedadId) {
        List<Contrato> contratos = contratoRepo.findByEliminadoFalseOrderByIdAsc();
        for (Contrato c : contratos) {
            if (c.getPropiedad() != null && c.getPropiedad().getId().equals(propiedadId) && c.getEstado() == EstadoContrato.ACTIVO) return true;
        }
        return false;
    }

    private void validar(Propiedad propiedad, Long propietarioId, Long ciudadId, Long idActual) throws Excepcion {
        if (propiedad.getDireccion() == null || propiedad.getDireccion().trim().isEmpty()) throw new Excepcion("La dirección es obligatoria", "direccion");
        if (propiedad.getTipo() == null) throw new Excepcion("Debe seleccionar un tipo", "tipo");
        if (propiedad.getCantidadAmbientes() == null || propiedad.getCantidadAmbientes() <= 0) throw new Excepcion("La cantidad de ambientes debe ser positiva", "cantidadAmbientes");
        if (propiedad.getMetrosCuadrados() == null || propiedad.getMetrosCuadrados() <= 0) throw new Excepcion("Los metros cuadrados deben ser positivos", "metrosCuadrados");
        if (propiedad.getDescripcion() == null || propiedad.getDescripcion().trim().isEmpty())
            throw new Excepcion("La descripción es obligatoria", "descripcion");
        if (propietarioId == null)
            throw new Excepcion("Debe seleccionar un propietario", "propietarioId");
        if (ciudadId == null) throw new Excepcion("Debe seleccionar una ciudad", "ciudadId");
        Persona propietario = personaRepo.findById(propietarioId).orElse(null);
        if (propietario == null || Boolean.TRUE.equals(propietario.getEliminada())) throw new Excepcion("Debe seleccionar un propietario válido", "propietarioId");
        if (!ciudadRepo.existsById(ciudadId)) throw new Excepcion("Debe seleccionar una ciudad válida", "ciudadId");
        List<Propiedad> propiedades = propiedadRepo.findByEliminadaFalseOrderByIdAsc();
        for (Propiedad p : propiedades) {
            boolean otra = idActual == null || !p.getId().equals(idActual);
            if (otra && p.getDireccion().equalsIgnoreCase(propiedad.getDireccion()) && p.getCiudad() != null && p.getCiudad().getId().equals(ciudadId)) {
                throw new Excepcion("No puede existir más de una propiedad activa con la misma dirección y ciudad", "direccion");
            }
        }
    }

    private void guardarHistorial(Propiedad propiedad, EstadoDisponibilidad estado) {
        HistorialEstadoPropiedad h = new HistorialEstadoPropiedad();
        h.setPropiedad(propiedad);
        h.setEstado(estado);
        h.setFechaHora(LocalDateTime.now());
        historialRepo.save(h);
    }
}
