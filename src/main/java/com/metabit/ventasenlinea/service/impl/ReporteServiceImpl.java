package com.metabit.ventasenlinea.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.metabit.ventasenlinea.service.ReporteService;

@Service("reporteServiceImpl")
public class ReporteServiceImpl implements ReporteService{

	@PersistenceContext
    EntityManager em;
	
	@Override
	public List<Object[]> ventasTotralPorDepartamento(String fechaInicio, String fechaFin, Integer idPais) {
		// TODO Auto-generated method stub
		String query = "SELECT\n" + 
				"	q2.id_departamento,\n" + 
				"	q2.nombre_departamento,\n" + 
				"	ROUND(SUM(q2.total), 2) total,\n" + 
				"	SUM(q2.cantidad) cantidad\n" + 
				"FROM\n" + 
				"	(\n" + 
				"	SELECT\n" + 
				"		d.id_departamento, d.nombre_departamento, q1.total, q1.cantidad, p3.titulo\n" + 
				"	FROM\n" + 
				"		departamentos d\n" + 
				"	INNER JOIN categorias c ON\n" + 
				"		d.id_departamento = c.id_departamento\n" + 
				"	INNER JOIN subcategorias s2 ON\n" + 
				"		c.id_categoria = s2.categoria_id_categoria\n" + 
				"	INNER JOIN productos p3 ON\n" + 
				"		s2.id_subcategoria = p3.id_subcategoria\n" + 
				"	INNER JOIN (\n" + 
				"		SELECT\n" + 
				"			ap.producto_id_articulo, ap.cantidad * ap.precio_unitario total, ap.cantidad\n" + 
				"		FROM\n" + 
				"			pedidos p2\n" + 
				"		INNER JOIN articulo_pedido ap ON\n" + 
				"			p2.id_pedido = ap.pedido_id_pedido\n" + 
				"		WHERE\n" + 
				"			p2.fecha_pedido BETWEEN ? AND ?\n" + 
				"			AND p2.pais_id_pais = ?) q1 ON\n" + 
				"		p3.id_articulo = q1.producto_id_articulo) q2\n" + 
				"GROUP BY\n" + 
				"	q2.id_departamento";
		Query q =   em.createNativeQuery(query);
		
		q.setParameter(1,fechaInicio);
		q.setParameter(2,fechaFin);
		q.setParameter(3,idPais);
		
		//List<Object[]> test =  q.getResultList();
		return q.getResultList();
	}

}
