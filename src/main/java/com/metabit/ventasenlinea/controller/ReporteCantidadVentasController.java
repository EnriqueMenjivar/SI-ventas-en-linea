package com.metabit.ventasenlinea.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.metabit.ventasenlinea.entity.ReporteCantidadVentas;
import com.metabit.ventasenlinea.mapper.ReporteCantidadVentasRowMapper;
import com.metabit.ventasenlinea.service.CategoriaService;
import com.metabit.ventasenlinea.service.DepartamentoService;

@Controller

@RequestMapping("/reporteCantidadVentas")
@PreAuthorize("hasRole('ROLE_ADMIN')")

public class ReporteCantidadVentasController {

	@Autowired
	@Qualifier("categoriaServiceImpl")
	private CategoriaService categoriaService;

	@Autowired
	@Qualifier("departamentoServiceImpl")
	private DepartamentoService departamentoService;

	private static final String INDEX_VIEW = "reporteCantidadVentas/index";
	private static final String REPORTE_VIEW = "reporteCantidadVentas/reporteCantidadVentas";

	@GetMapping("/index")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView(INDEX_VIEW);
		return mav;
	}

	// método para recibir el reporte de tiempo
	@PostMapping("/procesar")
	public ModelAndView createCategoriaPost(@RequestParam String fechaInicio, @RequestParam String fechaFin) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/ventas");
		dataSource.setUsername("root");
		dataSource.setPassword("conejo22");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select categorias.nombre_categoria as categorias, round(sum(articulo_pedido.precio_unitario),2) as cantidad_en_ventas from pedidos "
				+ "inner join articulo_pedido on pedidos.id_pedido=articulo_pedido.pedido_id_pedido "
				+ "inner join productos on articulo_pedido.producto_id_articulo=productos.id_articulo "
				+ "inner join subcategorias on subcategorias.id_subcategoria=productos.id_subcategoria "
				+ "inner join categorias on categorias.id_categoria=subcategorias.categoria_id_categoria "
				+ "where fecha_pedido between ? and ? " + "group by categorias.nombre_categoria; ";
		List<ReporteCantidadVentas> reporte = jdbcTemplate.query(query, new ReporteCantidadVentasRowMapper(),
				fechaInicio, fechaFin);

		ModelAndView mav = new ModelAndView(REPORTE_VIEW);
		mav.addObject("reporte", reporte);
		mav.addObject("fechaInicio", fechaInicio);
		mav.addObject("fechaFin", fechaFin);
		System.out.println("el numero de elementos es: " + reporte.size());
		return mav;

	}

}
