package com.metabit.ventasenlinea.service;

import java.util.List;

import com.metabit.ventasenlinea.entity.ArticuloPedido;

public interface ArticuloPedidoService {
	public abstract ArticuloPedido createArticuloPedido(ArticuloPedido ap);
	
	public abstract List<ArticuloPedido> getAll();
}
