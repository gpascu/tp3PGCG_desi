package com.pgcg.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgcg.accesoDatos.ICiudadRepo;
import com.pgcg.accesoDatos.IProvinciaRepo;
import com.pgcg.entidades.Provincia;
import com.pgcg.excepciones.EntidadNoEncontradaException;
import com.pgcg.excepciones.Excepcion;

@Service
public class ProvinciaServiceImpl implements ProvinciaService {

    @Autowired
    private IProvinciaRepo provinciaRepo;

    @Autowired
    private ICiudadRepo ciudadRepo;

    @Override
    public List<Provincia> listarActivas() {
        return provinciaRepo.findByEliminadaFalseOrderByNombreAsc();
    }

    @Override
    public Provincia buscarPorId(Long id) {
        return provinciaRepo.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Provincia", id));
    }

    @Override
    public void registrar(Provincia provincia) throws Excepcion {
        validar(provincia, null);
        provincia.setEliminada(false);
        provinciaRepo.save(provincia);
    }

    @Override
    public void editar(Provincia provincia) throws Excepcion {
        Provincia actual = buscarPorId(provincia.getId());
        validar(provincia, provincia.getId());
        actual.setNombre(provincia.getNombre());
        provinciaRepo.save(actual);
    }

    @Override
    public void eliminar(Long id) throws Excepcion {
        Provincia provincia = buscarPorId(id);
        if (ciudadRepo.countByProvinciaIdAndEliminadaFalse(id) > 0) {
            throw new Excepcion("No se puede eliminar la provincia porque tiene ciudades asociadas");
        }
        provincia.setEliminada(true);
        provinciaRepo.save(provincia);
    }

    private void validar(Provincia provincia, Long idActual) throws Excepcion {
        if (provincia.getNombre() == null || provincia.getNombre().trim().isEmpty()) {
            throw new Excepcion("El nombre de la provincia es obligatorio", "nombre");
        }
        List<Provincia> provincias = provinciaRepo.findByEliminadaFalseOrderByNombreAsc();
        for (Provincia p : provincias) {
            boolean otra = idActual == null || !p.getId().equals(idActual);
            if (otra && p.getNombre().equalsIgnoreCase(provincia.getNombre())) {
                throw new Excepcion("Ya existe una provincia activa con ese nombre", "nombre");
            }
        }
    }
}
