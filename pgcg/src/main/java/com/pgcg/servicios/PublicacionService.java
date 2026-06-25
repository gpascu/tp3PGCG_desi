package com.pgcg.servicios;

import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.Publicacion;
import com.pgcg.accesoDatos.IPublicacionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

public interface PublicacionService {
    List<Publicacion> listarTodas();
    Publicacion guardar(Publicacion publicacion);
    Publicacion buscarPorId(Long id);
    void eliminar(Long id);
	List<Publicacion> buscarConFiltros(Long id, EstadoPublicacion estado);
	Publicacion modificar(Long id, Publicacion publicacionModificada);
	
}