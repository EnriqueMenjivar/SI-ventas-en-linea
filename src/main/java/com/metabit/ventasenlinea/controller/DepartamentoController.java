package com.metabit.ventasenlinea.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.metabit.ventasenlinea.entity.Departamento;
import com.metabit.ventasenlinea.entity.Pais;
import com.metabit.ventasenlinea.service.DepartamentoService;
import com.metabit.ventasenlinea.service.PaisService;
import com.metabit.ventasenlinea.service.ReporteService;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/departamento")
public class DepartamentoController {

	// LOG
	private static final Log LOGGER = LogFactory.getLog(DepartamentoController.class);

	@Autowired
	@Qualifier("departamentoServiceImpl")
	private DepartamentoService departamentoService;
	
	@Autowired
	@Qualifier("paisServiceImpl")
	private PaisService paisService;
	
	@Autowired
	@Qualifier("reporteServiceImpl")
	private ReporteService reporteService;
	

	private static final String INDEX_VIEW = "departamento/index";
	private static final String CREAR_DEPARTAMENTO_VIEW = "departamento/crearDepartamento";
	private static final String PARAM_CONFIG_VIEW = "departamento/param_config";
	private static final String VENTAS_DEPARTAMENTO = "departamento/ventas_por_departamento";
	
	@GetMapping("/listar")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView(INDEX_VIEW);
		mav.addObject("departamentos", departamentoService.getDepartamentos());
		// user
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("user", user.getUsername());
		mav.addObject("role", user.getAuthorities().toArray()[0].toString());
		return mav;
	}

	@RequestMapping(path = {"/form-departamento", "/form-departamento/{id}"})
	public ModelAndView crearDepartamento(@PathVariable("id") Optional<Integer> id) {
		ModelAndView mav = new ModelAndView(CREAR_DEPARTAMENTO_VIEW);
		if(id.isPresent()) {
			//actualizamos 
			Integer num = Integer.valueOf(id.get());
			Departamento departamento = departamentoService.getDepartamento(num);
			mav.addObject("departamento", departamento);
		}else {
			// departamento empty para crear departamentos
			mav.addObject("departamento", new Departamento());
		}
		
		// user
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("user", user.getUsername());
		mav.addObject("role", user.getAuthorities().toArray()[0].toString());
		return mav;
	}

	// método para recibir el post del formulario de crear departamento
	@PostMapping("/crear-departamento-post")
	public String createDepartamentoPost(@Valid @ModelAttribute("departamento") Departamento departamento,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/departamento/crearDepartamento";
		} else {
			if(departamento.getId() == 0) {
				departamento.setHabilitado(true);
			}
			departamentoService.createDepartamento(departamento);
			return "redirect:/departamento/listar";
		}

	}

	// método para recibir el post de deshabilitar departamento post
	@PostMapping("/deshabilitar-departamento-post")
	public String deshabilitarDepartamentoPost(@RequestParam("id_departamento") int id_departamento) {
		LOGGER.info("mensaje: " + id_departamento);
		Departamento departamento = departamentoService.getDepartamento(id_departamento);
		LOGGER.info("departamento: " + departamento);
		departamento.setHabilitado(false);
		departamentoService.updateDepartamento(departamento);
		return "redirect:/departamento/listar";

	}

	// método para recibir el post de habilitar departamento post
	@PostMapping("/habilitar-departamento-post")
	public String habilitarDepartamentoPost(@RequestParam("id_departamento") int id_departamento) {
		LOGGER.info("mensaje: " + id_departamento);
		Departamento departamento = departamentoService.getDepartamento(id_departamento);
		LOGGER.info("departamento: " + departamento);
		departamento.setHabilitado(true);
		departamentoService.updateDepartamento(departamento);
		return "redirect:/departamento/listar";

	}
	
	@GetMapping("/ventas-por-departamento-config")
	public ModelAndView ventasPorDepartamentoConfig() {
		ModelAndView mav = new ModelAndView(PARAM_CONFIG_VIEW);
		
		mav.addObject("paises", paisService.getAllPais());
		
		return mav;
	}
	
	@GetMapping("/ventas-por-departamento")
	public ModelAndView ventasPorDepartamento(
			@RequestParam("fechaInicio") String fechaInicio,
			@RequestParam("fechaFin") String fechaFin,
			@RequestParam("pais") Integer idPais,
			@RequestParam("idPDF") Integer isPDF){
		
		ModelAndView mav = new ModelAndView(VENTAS_DEPARTAMENTO);
		
		Pais pais = paisService.getPaisById(idPais);
		List<Object[]> vetasDepartamento = reporteService.ventasTotralPorDepartamento(fechaInicio, fechaFin, idPais);
		
		mav.addObject("ventasDepartamento", vetasDepartamento);
		mav.addObject("fechaInicio", fechaInicio);
		mav.addObject("fechaFin", fechaFin);
		mav.addObject("pais", pais);
		mav.addObject("isPDF", isPDF);
		
		return mav;
	}
	
}
