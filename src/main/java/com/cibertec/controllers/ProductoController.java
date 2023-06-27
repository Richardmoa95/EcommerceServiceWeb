package com.cibertec.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.model.Producto;
import com.cibertec.service.ProductoServiceImplements;

@RestController
@RequestMapping("/productos")
public class ProductoController {
	
	private final ProductoServiceImplements productoService;

    public ProductoController(ProductoServiceImplements productoService) {
        this.productoService = productoService;
    }

    //Traer todos los productos
    @GetMapping("")
	public ResponseEntity<List<Producto>> getAllProductos() {
		List<Producto> productos = productoService.findAll();
		return ResponseEntity.ok(productos);
	}
    
    //Traer un producto por id
    @GetMapping("/{id}")
	public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
		Optional<Producto> optionalProducto = productoService.getById(id);
		if (optionalProducto.isPresent()) {
			Producto producto = optionalProducto.get();
			return ResponseEntity.ok(producto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
    
    //Crear producto
    @PostMapping("")
	public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
		// Guardar el producto en la base de datos
		Producto createdProducto = productoService.save(producto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProducto);
	}
    
    @PutMapping("/{id}")
	public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody Producto producto) {
		Optional<Producto> optionalProducto = productoService.getById(id);
		if (optionalProducto.isPresent()) {
			Producto existingProducto = optionalProducto.get();
			// Actualizar los demás campos del producto según sea necesario
			existingProducto.setCantidad(producto.getCantidad());
			existingProducto.setDescripcion(producto.getDescripcion());
			existingProducto.setNombre(producto.getNombre());
			existingProducto.setPrecio(producto.getPrecio());
			// Guardar el producto actualizado en la base de datos
			Producto updatedProducto = productoService.update(existingProducto);
			return ResponseEntity.ok(updatedProducto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProducto(@PathVariable Integer id) {
		Optional<Producto> optionalProducto = productoService.getById(id);
		if (optionalProducto.isPresent()) {
			// Eliminar el producto de la base de datos
			productoService.delete(id);
			return ResponseEntity.ok("Producto Eliminado");
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
