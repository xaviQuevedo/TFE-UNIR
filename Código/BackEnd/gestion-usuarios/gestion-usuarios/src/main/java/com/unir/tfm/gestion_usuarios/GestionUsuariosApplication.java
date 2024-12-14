package com.unir.tfm.gestion_usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.unir.tfm.gestion_usuarios"})
public class GestionUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionUsuariosApplication.class, args);

		/* // Crear una instancia de BCryptPasswordEncoder
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// Contraseña que quieres codificar
		String rawPassword = "contraseña_segura";

		// Codificar la contraseña
		String encodedPassword = encoder.encode(rawPassword);

		// Imprimir la contraseña codificada
		System.out.println("Contraseña codificada: " + encodedPassword); */
	}

}
