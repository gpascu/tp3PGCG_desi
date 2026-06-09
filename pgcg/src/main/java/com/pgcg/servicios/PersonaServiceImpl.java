package com.pgcg.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pgcg.accesoDatos.IPersonaRepo;
import com.pgcg.entidades.Persona;

@Service
public class PersonaServiceImpl implements PersonaService {
    @Autowired
    private IPersonaRepo personaRepo;

    public List<Persona> listarActivas() {
        return personaRepo.findByEliminadaFalseOrderByApellidoAscNombreAsc();
    }
}
