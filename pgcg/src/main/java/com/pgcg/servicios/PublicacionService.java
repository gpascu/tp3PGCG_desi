package com.pgcg.servicios;

import com.pgcg.entidades.EstadoPublicacion;
import com.pgcg.entidades.HistorialEstadoPublicacion;
import com.pgcg.entidades.Publicacion;
import java.math.BigDecimal;
import java.util.List;

public interface PublicacionService {
    List<Publicacion> listarTodas();
    Publicacion guardar(Publicacion publicacion);
    Publicacion buscarPorId(Long id);
    void eliminar(Long id);
	List<Publicacion> buscarConFiltros(Long propiedadId, Long ciudadId, EstadoPublicacion estado,
	        BigDecimal precioMin, BigDecimal precioMax);
	void finalizarPublicacionesDePropiedad(Long propiedadId);
	List<HistorialEstadoPublicacion> listarHistorialEstados(Long publicacionId);

}