package com.metabit.ventasenlinea.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="productos")
public class Producto {
	
	//Falta el campo para la llave foranea de subgategoria
	
	@Id
	@GeneratedValue
	@Column(name="id_articulo")
	private int idArticulo;
	
	@Column(name="imagen")
	private String imagen;
	
	@Column(name="marca",length=50)
	private String marca;
	
	@Column(name="titulo", length=50)
	private String titulo;
	
	@Column(name="margen_ganancia")
	private double margenGanancia;
	
	@Column(name="porcentaje_descuento")
	private double porcentajeDescuento;
	
	@Column(name="descripcion_articulo")
	private String descripcionArticulo;

	public Producto() {}

	public Producto(int idArticulo, String imagen, String marca, String titulo, double margenGanancia,
			double porcentajeDescuento, String descripcionArticulo) {
		this.idArticulo = idArticulo;
		this.imagen = imagen;
		this.marca = marca;
		this.titulo = titulo;
		this.margenGanancia = margenGanancia;
		this.porcentajeDescuento = porcentajeDescuento;
		this.descripcionArticulo = descripcionArticulo;
	}

	public int getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}
	

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public double getMargenGanancia() {
		return margenGanancia;
	}

	public void setMargenGanancia(double margenGanancia) {
		this.margenGanancia = margenGanancia;
	}

	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
}