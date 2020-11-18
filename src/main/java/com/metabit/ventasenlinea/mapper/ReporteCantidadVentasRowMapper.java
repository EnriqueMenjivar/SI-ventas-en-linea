package com.metabit.ventasenlinea.mapper;
import com.metabit.ventasenlinea.entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ReporteCantidadVentasRowMapper implements RowMapper<ReporteCantidadVentas> {
	
	public ReporteCantidadVentas mapRow(ResultSet rs, int rowNum) throws SQLException {
		 ReporteCantidadVentas obj = new ReporteCantidadVentas();
		 String categoria = rs.getString("categorias");
		 float cantidad = rs.getFloat("cantidad_en_ventas");
		 obj.setCategoria(categoria);
		 obj.setCantidadEnVentas(cantidad);
		 
		 return obj;
		
	}

}
