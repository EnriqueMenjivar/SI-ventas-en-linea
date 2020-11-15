package com.metabit.ventasenlinea.service;

import java.util.List;

public interface ReporteService {

	public abstract List<Object[]> ventasTotralPorDepartamento(String fechaInicio, String fechaFin, Integer idPais);
}
