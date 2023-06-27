package com.cibertec.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.model.DetalleOrden;
import com.cibertec.model.Orden;
import com.cibertec.model.Producto;
import com.cibertec.model.Usuario;
import com.cibertec.service.OrdenServiceImplements;
import com.cibertec.service.ProductoServiceImplements;
import com.cibertec.service.UsuarioServiceImplements;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

	@Autowired
	private ProductoServiceImplements productoService;
	
	@Autowired
	private UsuarioServiceImplements usuarioService;
	
	@Autowired
	private OrdenServiceImplements ordenService;
	
	@GetMapping("")
	public ResponseEntity<List<Producto>> getHome() {
		List<Producto> productos = productoService.findAll();
		return ResponseEntity.ok().body(productos);
	}
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> getUsuarios() {
		List<Usuario> usuarios = usuarioService.findAll();
		return ResponseEntity.ok().body(usuarios);
	}

	@GetMapping("/ordenes")
	public ResponseEntity<List<Orden>> getOrdenes() {
		List<Orden> ordenes = ordenService.findAll();
		return ResponseEntity.ok().body(ordenes);
	}

	@GetMapping("/detalle/{id}")
	public ResponseEntity<List<DetalleOrden>> getDetalle(@PathVariable Integer id) {
		Orden orden = ordenService.findById(id).orElse(null);
		if (orden == null) {
			return ResponseEntity.notFound().build();
		}
		List<DetalleOrden> detalles = orden.getDetalle();
		return ResponseEntity.ok().body(detalles);
	}
}
