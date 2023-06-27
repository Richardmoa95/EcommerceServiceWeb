package com.cibertec.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.model.DetalleOrden;
import com.cibertec.model.Orden;
import com.cibertec.model.Producto;
import com.cibertec.model.Usuario;
import com.cibertec.service.DetalleOrdenImplements;
import com.cibertec.service.OrdenServiceImplements;
import com.cibertec.service.ProductoServiceImplements;
import com.cibertec.service.UsuarioServiceImplements;

@RestController
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private ProductoServiceImplements productoService;
	
	@Autowired
	private UsuarioServiceImplements usuarioService;
	
	@Autowired
	private OrdenServiceImplements ordenService;
	
	@Autowired
	private DetalleOrdenImplements detalleOrdenService;
	
	// Para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	// Datos de la orden
	Orden orden = new Orden();
	
	@PostMapping("/cart")
	public ResponseEntity<List<DetalleOrden>> addToCart(@RequestParam Integer id, @RequestParam Integer cantidad) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto> optionalProducto = productoService.getById(id);
		if (optionalProducto.isPresent()) {
			producto = optionalProducto.get();
			
			detalleOrden.setCantidad(cantidad);
			detalleOrden.setPrecio(producto.getPrecio());
			detalleOrden.setNombre(producto.getNombre());
			detalleOrden.setTotal(producto.getPrecio() * cantidad);
			detalleOrden.setProducto(producto);
			
			// Validar que el producto no se añada 2 veces
			Integer idProducto = producto.getId();
			boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
			
			if (!ingresado) {
				detalles.add(detalleOrden);
			}
			
			sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
			orden.setTotal(sumaTotal);
			
			return ResponseEntity.ok().body(detalles);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/cart/{id}")
	public ResponseEntity<List<DetalleOrden>> removeProductFromCart(@PathVariable Integer id) {
		detalles = detalles.stream().filter(dt -> dt.getProducto().getId() != id).collect(Collectors.toList());
		
		double sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		
		return ResponseEntity.ok().body(detalles);
	}
	
	@GetMapping("/cart")
	public ResponseEntity<List<DetalleOrden>> getCart() {
		return ResponseEntity.ok().body(detalles);
	}
	
	@GetMapping("/order")
	public ResponseEntity<String> order(Integer idUsuario) {
		Usuario usuario = usuarioService.findById(idUsuario).get();
		orden.setUsuario(usuario);
		orden.setFechaCreacion(new Date());
		orden.setNumero(ordenService.generarNumeroOrden());
		
		ordenService.save(orden);
		
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		orden = new Orden();
		detalles.clear();
		
		return ResponseEntity.ok().body("Orden guardada exitosamente.");
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Producto>> searchProduct(@RequestParam String nombre) {
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().contains(nombre))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(productos);
	}
}

