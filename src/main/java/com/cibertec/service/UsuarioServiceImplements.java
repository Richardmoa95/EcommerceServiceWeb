package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibertec.model.Usuario;
import com.cibertec.repository.UsuarioRepository;

@Service
public class UsuarioServiceImplements{

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServiceImplements(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
    //Guardar la data del usuario y cambiar por passwordEncoder la contra
    public Usuario saveUser(Usuario user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return usuarioRepository.save(user);
    }

    //Obtener la data del usuario por su correo
    public Usuario getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}
    
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
}
