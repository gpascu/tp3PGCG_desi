package com.pgcg.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgcg.accesoDatos.ICiudadRepo;
import com.pgcg.accesoDatos.IContratoRepo;
import com.pgcg.accesoDatos.IPersonaRepo;
import com.pgcg.accesoDatos.IPropiedadRepo;
import com.pgcg.entidades.Ciudad;
import com.pgcg.entidades.Persona;
import com.pgcg.excepciones.EntidadNoEncontradaException;
import com.pgcg.excepciones.Excepcion;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private IPersonaRepo personaRepo;

    @Autowired
    private ICiudadRepo ciudadRepo;

    @Autowired
    private IPropiedadRepo propiedadRepo;

    @Autowired
    private IContratoRepo contratoRepo;

    @Override
    public List<Persona> listarActivas() {
        return personaRepo.findByEliminadaFalseOrderByApellidoAscNombreAsc();
    }

    @Override
    public Persona buscarPorId(Long id) {
        return personaRepo.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Persona", id));
    }

    @Override
    public void registrar(Persona persona, Long ciudadId) throws Excepcion {
        validar(persona, ciudadId, null);
        Ciudad ciudad = ciudadRepo.findById(ciudadId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Ciudad", ciudadId));
        persona.setCiudad(ciudad);
        persona.setEliminada(false);
        personaRepo.save(persona);
    }

    @Override
    public void editar(Persona persona, Long ciudadId) throws Excepcion {
        Persona actual = buscarPorId(persona.getId());
        validar(persona, ciudadId, persona.getId());
        Ciudad ciudad = ciudadRepo.findById(ciudadId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Ciudad", ciudadId));
        actual.setNombre(persona.getNombre());
        actual.setApellido(persona.getApellido());
        actual.setDniCuit(persona.getDniCuit());
        actual.setTelefono(persona.getTelefono());
        actual.setEmail(persona.getEmail());
        actual.setDomicilio(persona.getDomicilio());
        actual.setCiudad(ciudad);
        personaRepo.save(actual);
    }

    @Override
    public void eliminar(Long id) throws Excepcion {
        Persona persona = buscarPorId(id);
        if (propiedadRepo.countByPropietarioIdAndEliminadaFalse(id) > 0) {
            throw new Excepcion("No se puede eliminar la persona porque tiene propiedades asociadas");
        }
        if (contratoRepo.countByInquilinoIdAndEliminadoFalse(id) > 0) {
            throw new Excepcion("No se puede eliminar la persona porque tiene contratos asociados");
        }
        persona.setEliminada(true);
        personaRepo.save(persona);
    }

    private void validar(Persona persona, Long ciudadId, Long idActual) throws Excepcion {
        if (persona.getNombre() == null || persona.getNombre().trim().isEmpty()) {
            throw new Excepcion("El nombre es obligatorio", "nombre");
        }
        if (persona.getApellido() == null || persona.getApellido().trim().isEmpty()) {
            throw new Excepcion("El apellido es obligatorio", "apellido");
        }
        if (persona.getDniCuit() == null || persona.getDniCuit().trim().isEmpty()) {
            throw new Excepcion("El DNI/CUIT es obligatorio", "dniCuit");
        }
        if (persona.getTelefono() == null || persona.getTelefono().trim().isEmpty()) {
            throw new Excepcion("El teléfono es obligatorio", "telefono");
        }
        if (persona.getEmail() == null || persona.getEmail().trim().isEmpty()) {
            throw new Excepcion("El email es obligatorio", "email");
        }
        if (persona.getDomicilio() == null || persona.getDomicilio().trim().isEmpty()) {
            throw new Excepcion("El domicilio es obligatorio", "domicilio");
        }
        if (ciudadId == null) {
            throw new Excepcion("Debe seleccionar una ciudad", "ciudadId");
        }
        Ciudad ciudad = ciudadRepo.findById(ciudadId).orElse(null);
        if (ciudad == null || Boolean.TRUE.equals(ciudad.getEliminada())) {
            throw new Excepcion("Debe seleccionar una ciudad válida", "ciudadId");
        }
        List<Persona> personas = personaRepo.findByEliminadaFalseOrderByApellidoAscNombreAsc();
        for (Persona p : personas) {
            boolean otra = idActual == null || !p.getId().equals(idActual);
            if (otra && p.getDniCuit().equalsIgnoreCase(persona.getDniCuit())) {
                throw new Excepcion("Ya existe una persona activa con ese DNI/CUIT", "dniCuit");
            }
        }
    }
}
