package org.mitre.springboot.samples;

import org.mitre.springboot.config.annotation.EnableOpenIDConnectServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableOpenIDConnectServer
public class DefaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefaultApplication.class, args);
	}
}
