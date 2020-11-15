package com.metabit.ventasenlinea.service;

import java.util.Date;
import java.util.List;

import com.metabit.ventasenlinea.entity.Cliente;
import com.metabit.ventasenlinea.entity.Estado;
import com.metabit.ventasenlinea.entity.Pedido;

public interface PedidoService {
	
	public List<Pedido> getAllPedidosCliente(Cliente id_cliente);
	
	public List<Pedido> getAllPedidosEmploye(Estado id_estado);
	
	public List<Pedido> getAll();
	
	public Pedido findById(int pedido_id);
	
	public Pedido updatePedido(Pedido pedido);

	public Pedido getPedido(int id_pedido);
	
	public Pedido createPedido(Pedido pedido);
	
	public abstract Pedido getUltimoPedido();
	
	public abstract List<Pedido> findByCounterAndDates(Date fechaInicio, Date fechaFin, int idPais);
}
