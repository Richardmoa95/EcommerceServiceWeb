package com.cibertec.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.model.DetalleOrden;
import com.cibertec.model.Orden;
import com.cibertec.model.Usuario;
import com.cibertec.service.OrdenServiceImplements;
import com.cibertec.service.UsuarioServiceImplements;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	//Inyecciones
    private final UsuarioServiceImplements userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrdenServiceImplements ordenService;

    public LoginController(UsuarioServiceImplements userService, BCryptPasswordEncoder passwordEncoder,OrdenServiceImplements ordenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.ordenService = ordenService;
    }

    //Método Registrar del Login
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usu) {
    	Usuario usuRegistrado = userService.saveUser(usu);
    	if (usuRegistrado != null) {
    		return ResponseEntity.ok("Registrado");	
    	} else {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el registro");
    	}
    }

    //Método Iniciar Sesión del Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario user) {
    	Usuario storedUser = userService.getUserByEmail(user.getEmail());
        if (storedUser != null && passwordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
        	if (storedUser.getTipo().equals("ADMIN")) {
				return ResponseEntity.ok("Login exitoso como administrador");
			} else {
				return ResponseEntity.ok("Login exitoso como usuario");
			}
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    
    //Método para obtener las compras del usuario
    @GetMapping("/compras")
	public ResponseEntity<List<Orden>> obtenerCompras(@RequestBody Usuario user) {
		Usuario usuario = userService.getUserByEmail(user.getEmail());
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		return ResponseEntity.ok(ordenes);
	}

    //Obtener el detalle de una orden con su id
    @GetMapping("/detalle/{id}")
	public ResponseEntity<List<DetalleOrden>> detalleCompra(@PathVariable Integer id) {
    	Optional<Orden> orden = ordenService.findById(id);
		if (orden == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		if (orden.isPresent()) {
			return ResponseEntity.ok(orden.get().getDetalle());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}