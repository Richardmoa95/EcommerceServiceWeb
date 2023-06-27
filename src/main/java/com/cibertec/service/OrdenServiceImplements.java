package com.cibertec.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cibertec.model.Orden;
import com.cibertec.model.Usuario;
import com.cibertec.repository.OrdenRepository;

@Service
public class OrdenServiceImplements {
	
    private final OrdenRepository ordenRepository;

    public OrdenServiceImplements(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }
    
    public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}
    
	public Optional<Orden> findById(Integer id) {
		
		return ordenRepository.findById(id);
	}
	
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}
	
	public String generarNumeroOrden() {
		int numero=0;
		String numeroConcatenado="";
		
		List<Orden> ordenes = findAll();
		List<Integer> numeros= new ArrayList<Integer>();
		
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero=1;
		}else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		if (numero<10) { //0000000001
			numeroConcatenado="000000000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado="00000000"+String.valueOf(numero);
		}
		else if(numero<1000) {
			numeroConcatenado="0000000"+String.valueOf(numero);
		}
		else if(numero<10000) {
			numeroConcatenado="000000"+String.valueOf(numero);
		}
		return numeroConcatenado;
	}
	
	public Orden save(Orden orden) {
		
		return ordenRepository.save(orden);
	}
}
