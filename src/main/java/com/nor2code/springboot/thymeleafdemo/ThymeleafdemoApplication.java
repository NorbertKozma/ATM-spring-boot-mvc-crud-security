package com.nor2code.springboot.thymeleafdemo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
@EnableEncryptableProperties
public class ThymeleafdemoApplication {

	public static void main(String[] args) {
		//SpringApplication.run(ThymeleafdemoApplication.class, args);
		SpringApplication app = new SpringApplication(ThymeleafdemoApplication.class);

		// Railway által biztosított port, vagy fallback 8080
		String port = System.getenv("PORT");
		if (port != null) {
			app.setDefaultProperties(Collections.singletonMap("server.port", port));
		}

		app.run(args);
	}
}

