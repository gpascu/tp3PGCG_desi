package com.pgcg.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgcg.accesoDatos.ICiudadRepo;
import com.pgcg.accesoDatos.IPersonaRepo;
import com.pgcg.accesoDatos.IPropiedadRepo;
import com.pgcg.accesoDatos.IProvinciaRepo;
import com.pgcg.entidades.Ciudad;
import com.pgcg.entidades.Provincia;
import com.pgcg.excepciones.EntidadNoEncontradaException;
import com.pgcg.excepciones.Excepcion;

@Service
public class CiudadServiceImpl implements CiudadService {

    @Autowired
    private ICiudadRepo ciudadRepo;

    @Autowired
    private IProvinciaRepo provinciaRepo;

    @Autowired
    private IPersonaRepo personaRepo;

    @Autowired
    private IPropiedadRepo propiedadRepo;

    @Override
    public List<Ciudad> listarActivas() {
        return ciudadRepo.findByEliminadaFalseOrderByNombreAsc();
    }

    @Override
    public Ciudad buscarPorId(Long id) {
        return ciudadRepo.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Ciudad", id));
    }

    @Override
    public void registrar(Ciudad ciudad, Long provinciaId) throws Excepcion {
        validar(ciudad, provinciaId, null);
        Provincia provincia = provinciaRepo.findById(provinciaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Provincia", provinciaId));
        ciudad.setProvincia(provincia);
        // Cuando registro una ciudad la dejo activa por defecto
        ciudad.setEliminada(false);
        ciudadRepo.save(ciudad);
    }

    @Override
    public void editar(Ciudad ciudad, Long provinciaId) throws Excepcion {
    	// Obtengo la ciudad guardada para actualizar sus datos
        Ciudad actual = buscarPorId(ciudad.getId());
        validar(ciudad, provinciaId, ciudad.getId());
        Provincia provincia = provinciaRepo.findById(provinciaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Provincia", provinciaId));
        actual.setNombre(ciudad.getNombre());
        actual.setProvincia(provincia);
        ciudadRepo.save(actual);
    }

    @Override
    public void eliminar(Long id) throws Excepcion {
        Ciudad ciudad = buscarPorId(id);
        // Verifico que no tenga personas asociadas
        if (personaRepo.countByCiudadIdAndEliminadaFalse(id) > 0) {
            throw new Excepcion("No se puede eliminar la ciudad porque tiene personas asociadas");
        }
        // Verifico que no tenga propiedades asociadas
        if (propiedadRepo.countByCiudadIdAndEliminadaFalse(id) > 0) {
            throw new Excepcion("No se puede eliminar la ciudad porque tiene propiedades asociadas");
        }
        ciudad.setEliminada(true);// Realizo la baja lógica en lugar de borrarla físicamente
        ciudadRepo.save(ciudad);
    }

    private void validar(Ciudad ciudad, Long provinciaId, Long idActual) throws Excepcion {
        if (ciudad.getNombre() == null || ciudad.getNombre().trim().isEmpty()) {
            throw new Excepcion("El nombre de la ciudad es obligatorio", "nombre");
        }
        if (provinciaId == null) {
            throw new Excepcion("Debe seleccionar una provincia", "provinciaId");
        }
        Provincia provincia = provinciaRepo.findById(provinciaId).orElse(null);
        // Verifico que la provincia exista y no esté eliminada
        if (provincia == null || Boolean.TRUE.equals(provincia.getEliminada())) {
            throw new Excepcion("Debe seleccionar una provincia válida", "provinciaId");
        }
        List<Ciudad> listaCiudades = ciudadRepo.findByEliminadaFalseOrderByNombreAsc();
        for (Ciudad ciudadActual : listaCiudades) {
            boolean otra = idActual == null || !ciudadActual.getId().equals(idActual);// Cuando estoy editando ignoro la misma ciudad
            // Verifico que no exista otra ciudad activa con el mismo nombre dentro de la provincia seleccionada
            if (otra && ciudadActual.getNombre().equalsIgnoreCase(ciudad.getNombre())
                    && ciudadActual.getProvincia() != null && ciudadActual.getProvincia().getId().equals(provinciaId)) {
                throw new Excepcion("Ya existe una ciudad activa con ese nombre en la provincia que ha seleccionado!", "nombre");
            }
        }
    }
}
