package com.metabit.ventasenlinea.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.metabit.ventasenlinea.entity.Cliente;
import com.metabit.ventasenlinea.entity.Estado;
import com.metabit.ventasenlinea.entity.Pedido;

@Repository("pedidoJpaRepository")
public interface PedidoJpaRepository extends JpaRepository<Pedido, Serializable> {
	
	public List<Pedido> findByCliente(Cliente id_cliente);
	
	public List<Pedido> findByEstado(Estado id_estado);
	
	public Pedido findByIdPedido(int id_pedido);
	
	public List<Pedido> findTopByOrderByIdPedidoDesc();
	
	@Query(value = "SELECT * FROM pedidos WHERE fecha_pedido between '?1' AND '?2' AND pais_id_pais=?3", nativeQuery = true)
	public List<Pedido> findByCountryAndDates(Date fechaInicio, Date fechaFi, int idPais);
}
