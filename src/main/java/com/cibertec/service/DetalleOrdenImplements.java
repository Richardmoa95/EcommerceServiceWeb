package com.cibertec.service;

import org.springframework.stereotype.Service;

import com.cibertec.model.DetalleOrden;
import com.cibertec.repository.DetalleOrdenRepository;

@Service
public class DetalleOrdenImplements {
	
    private final DetalleOrdenRepository detalleOrdenRepository;

    public DetalleOrdenImplements(DetalleOrdenRepository detalleOrdenRepository) {
        this.detalleOrdenRepository = detalleOrdenRepository;
    }
	
	public DetalleOrden save(DetalleOrden detalleOrden) {
		
		return detalleOrdenRepository.save(detalleOrden);
	}
}
