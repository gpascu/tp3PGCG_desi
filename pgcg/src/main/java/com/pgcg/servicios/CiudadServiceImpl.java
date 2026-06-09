package com.pgcg.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pgcg.accesoDatos.ICiudadRepo;
import com.pgcg.entidades.Ciudad;

@Service
public class CiudadServiceImpl implements CiudadService {
    @Autowired
    private ICiudadRepo ciudadRepo;

    public List<Ciudad> listarTodas() {
        return ciudadRepo.findAll();
    }
}
