package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cibertec.model.Producto;
import com.cibertec.repository.ProductoRepository;

@Service
public class ProductoServiceImplements {

	private final ProductoRepository productoRepository;
	
    public ProductoServiceImplements(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}
	
	public Optional<Producto> getById(Integer id) {
		return productoRepository.findById(id);
	}
	
	public Producto update(Producto producto) {
		return productoRepository.save(producto);
	}

	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}

	public List<Producto> findAll() {
		return productoRepository.findAll();
	}
}
