package com.metabit.ventasenlinea.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.metabit.ventasenlinea.entity.Producto;
import com.metabit.ventasenlinea.entity.ProductoCarrito;
import com.metabit.ventasenlinea.entity.Subcategoria;
import com.metabit.ventasenlinea.service.ProductoService;
import com.metabit.ventasenlinea.service.SubcategoriaService;

@Controller
@RequestMapping("/producto")
public class ProductoController {

	private static final String INDEX_VIEW = "/producto/index";
	ArrayList<ProductoCarrito> productosCarrito = new ArrayList<ProductoCarrito>();

	@Autowired
	@Qualifier("productoServiceImpl")
	private ProductoService productService;
	
	@Autowired
	@Qualifier("subcategoriaServiceImpl")
	private SubcategoriaService subcategoriaService;

	@GetMapping("/index")
	public ModelAndView indexProducto(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(INDEX_VIEW);
		HttpSession session = request.getSession();
		ArrayList<ProductoCarrito> getProductos;
		getProductos = (ArrayList<ProductoCarrito>)session.getAttribute("productosCarrito");

		// Borramos toda la BD para evitar que se repitan
		//productService.deleteAll();

		// Agregando productos a la BD
		/*Producto p1 = new Producto();
		p1.setImagen("/img_products/nintendo.jpg");
		p1.setMarca("Nintento");
		p1.setTitulo("Nintendo Switch");
		p1.setDescripcionArticulo("Consola de videojuego que puede jugarse en modo portatil y modo Dock");
		productService.addProduct(p1);

		Producto p2 = new Producto();
		p2.setImagen("/img_products/play.jpg");
		p2.setMarca("Sony");
		p2.setTitulo("Play Station 5");
		p2.setDescripcionArticulo("Consola de video juego con potencia para correr juegos en 4K");
		productService.addProduct(p2);

		Producto p3 = new Producto();
		p3.setImagen("/img_products/xbox.jpg");
		p3.setMarca("Microsoft");
		p3.setTitulo("Xbox One S");
		p3.setDescripcionArticulo("Consola con potencia para 4K, microprocesador Scorpio con 100teraflops");
		productService.addProduct(p3);*/

		/*
		 * Por si quieren probar con mas datos
		 * 
		 * 
		 * Producto p4 = new Producto(); p4.setImagen("/img_products/nintendo.jpg");
		 * p4.setMarca("Nintento"); p4.setTitulo("Nintendo Switch"); p4.
		 * setDescripcionArticulo("Consola de videojuego que puede jugarse en modo portatil y modo Dock"
		 * ); productService.addProduct(p4);
		 * 
		 * Producto p5 = new Producto(); p5.setImagen("/img_products/play.jpg");
		 * p5.setMarca("Sony"); p5.setTitulo("Play Station 5"); p5.
		 * setDescripcionArticulo("Consola de video juego con potencia para correr juegos en 4K"
		 * ); productService.addProduct(p5);
		 * 
		 * Producto p6 = new Producto(); p6.setImagen("/img_products/xbox.jpg");
		 * p6.setMarca("Microsoft"); p6.setTitulo("Xbox One S"); p6.
		 * setDescripcionArticulo("Consola con potencia para 4K, microprocesador Scorpio con 100teraflops"
		 * ); productService.addProduct(p6);
		 */

		// Recuperamos todos los datos de la BD
		
		/*if(getProductos != null) {
			for (ProductoCarrito pc : getProductos) {
				System.out.print("------------------------------------------------------"+pc.getProducto().getTitulo());
				System.out.println(" :"+pc.getCantidad());
			}
		}*/
		
		mav.addObject("productos", productService.getProductos());
		mav.addObject("esProducto", 1);

		// Si el usuario está autenticado devuelve a la vista el username y el role
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isUserLoggedIn()) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			mav.addObject("user", userDetail.getUsername());
			mav.addObject("role", userDetail.getAuthorities().toArray()[0].toString());
		}

		return mav;
	}

	//Devuelve true si el usuario ha iniciado sesión
	boolean isUserLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails;
	}
	
	/*@PostMapping("/agregar-producto")
	public String agregarProductoCarrito(HttpServletRequest request, @RequestParam("cantidad") int cantidad, @RequestParam("producto_id") int id) {
		ProductoCarrito productoCarrito = new ProductoCarrito();
		Producto producto = productService.findById(id);
		productoCarrito.setProducto(producto);
		productoCarrito.setCantidad(cantidad);
		
		HttpSession session = request.getSession(true);
		List<ProductoCarrito> productosSesion = (ArrayList<ProductoCarrito>)session.getAttribute("productosCarrito");
		
		if(productosSesion!=null) {
			if(!productosSesion.isEmpty()) {
				for(ProductoCarrito pc : productosSesion) {
					if(id == pc.getProducto().getIdArticulo()) {
						productosCarrito.remove(pc);
						break;
					}
				}
			}
		}
		
		productosCarrito.add(productoCarrito);
		session.setAttribute("productosCarrito", productosCarrito);
		
		return "redirect:/producto/index";
	}*/
	
	@GetMapping("/agregar-producto/{cantidad}/{id}")
	public @ResponseBody String agregarProductoCarrito(HttpServletRequest request, @PathVariable("cantidad") int cantidad, @PathVariable("id") int id) {
		ProductoCarrito productoCarrito = new ProductoCarrito();
		Producto producto = productService.findById(id);
		productoCarrito.setProducto(producto);
		productoCarrito.setCantidad(cantidad);
		HttpSession session = request.getSession(true);
		List<ProductoCarrito> productosSesion = (ArrayList<ProductoCarrito>)session.getAttribute("productosCarrito");
		
		if(productosSesion!=null) {
			if(!productosSesion.isEmpty()) {
				for(ProductoCarrito pc : productosSesion) {
					if(id == pc.getProducto().getIdArticulo()) {
						productosCarrito.remove(pc);
						break;
					}
				}
			}
		}
		
		productosCarrito.add(productoCarrito);
		session.setAttribute("productosCarrito", productosCarrito);
		
		return "OK";
	}
	
	@GetMapping("/remover-producto/{id}")
	public String removeProductoCarrito(@PathVariable("id") int id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<ProductoCarrito> productosSesion = (ArrayList<ProductoCarrito>)session.getAttribute("productosCarrito");
		
		for(ProductoCarrito pc : productosSesion) {
			if(id == pc.getProducto().getIdArticulo()) {
				productosSesion.remove(pc);

				break;
			}
		}
		
		return "redirect:/producto/index";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_VENTAS')")
	@GetMapping("/nuevo/{id}")
	public ModelAndView createProducto(@PathVariable("id") int idSubcategoria) {
		ModelAndView mav = new ModelAndView("producto/createProducto");
		
		mav.addObject("producto", new Producto());
		mav.addObject("idSubcateogria", idSubcategoria);
		
		return mav;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_VENTAS')")
	@PostMapping("/nuevo")
	public String storeProducto(
			@Valid @ModelAttribute("producto") Producto producto, 
			BindingResult bindingResult, 
			@RequestParam("image") MultipartFile image,
			@RequestParam("idSubcategoria") int idSubcategoria,
			RedirectAttributes redirAttrs) {
		String path;
		Subcategoria subcategoria = subcategoriaService.getSubcategoria(idSubcategoria); 
		
		if(bindingResult.hasErrors()) {
			return "producto/createProducto";
		}else {
			try {
				path = uploadImage(image);
				producto.setImagen(path);
				producto.setHabilitado(1);
				producto.setSubcategoria(subcategoria);
				productService.addProduct(producto);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			redirAttrs.addFlashAttribute("message", "Se ha registrado el producto con éxito");
			return "redirect:/producto/listar/"+subcategoria.getIdSubcategoria();
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_VENTAS')")
	@GetMapping("/actualizar/{id}")
	public ModelAndView editProducto(@PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView("producto/updateProducto");
		Producto producto = productService.findById(id);
		mav.addObject("producto", producto);
		
		return mav;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_VENTAS')")
	@PostMapping("/actualizar")
	public String updateProducto(@Valid @ModelAttribute("producto") Producto producto, BindingResult bindingResult, @RequestParam("image") MultipartFile image, RedirectAttributes redirAttrs) {
		String path;
		Producto p = productService.findById(producto.getIdArticulo());

		if(bindingResult.hasErrors()) {
			return "producto/updateProducto";
		}else {
			p.setTitulo(producto.getTitulo());
			p.setMarca(producto.getMarca());
			p.setMargenGanancia(producto.getMargenGanancia());
			p.setPorcentajeDescuento(producto.getPorcentajeDescuento());
			p.setDescripcionArticulo(producto.getDescripcionArticulo());
			
			if(image.isEmpty()) {
				p.setImagen(producto.getImagen());
				productService.updateProducto(p);
			}else {
				try {
					path = uploadImage(image);
					p.setImagen(path);
					productService.updateProducto(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			redirAttrs.addFlashAttribute("message", "Se ha actualizado el producto con éxito");
			return "redirect:/producto/listar";
		}
	}
	
	public String uploadImage(MultipartFile file) throws IOException{
		String UPLOAD_FOLDER = ".//src//main//resources//static//img_products//";
		
		byte[] bytes = file.getBytes();
		Path path = Paths.get(UPLOAD_FOLDER+file.getOriginalFilename());
		Files.write(path, bytes);
		
		return "/img_products/"+file.getOriginalFilename();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_VENTAS')")
	@GetMapping("/listar/{id}")
	public ModelAndView listProducto(@PathVariable("id") int idSubcategoria) {
		Subcategoria subcategoria = subcategoriaService.getSubcategoria(idSubcategoria);
		
		ModelAndView mav = new ModelAndView("/producto/listProducto");
		mav.addObject("subcategoria", subcategoria);
		
		return mav;
	}
	
	@PostMapping("/asignar-descuento")
	public String asignarDescuento(HttpServletRequest request) {
		int producto_id = Integer.parseInt(request.getParameter("producto_id"));
		double descuento = Double.parseDouble(request.getParameter("descuento"));
		Producto producto = productService.findById(producto_id);
		producto.setPorcentajeDescuento(descuento);
		productService.updateProducto(producto);
		
		return "redirect:/producto/listar";
	}
	
	@PostMapping("/hab-deshab")
	public String habDeshabProducto(HttpServletRequest request) {
		int producto_id = Integer.parseInt(request.getParameter("producto_id_hab"));
		Producto producto = productService.findById(producto_id);
		
		if(producto.getHabilitado() == 1)
			producto.setHabilitado(0);
		else
			producto.setHabilitado(1);
		
		productService.updateProducto(producto);
		
		return "redirect:/producto/listar";
	}
	
	@GetMapping("/ver-detalle/{id}")
	public ModelAndView verDetalleProducto(@PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView("producto/verDetalleProducto");
		Producto producto = productService.findById(id);
		mav.addObject("producto", producto);
		
		return mav;
	}
}
