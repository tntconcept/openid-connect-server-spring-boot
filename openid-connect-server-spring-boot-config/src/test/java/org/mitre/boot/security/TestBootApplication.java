package org.mitre.boot.security;

import org.mitre.springboot.config.annotation.EnableOpenIDConnectServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * @author barretttucker
 *
 */
@Configuration
@SpringBootApplication
@EnableOpenIDConnectServer
public class TestBootApplication extends SpringBootServletInitializer {
	
    public static void main(String[] args) {
        SpringApplication.run(TestBootApplication.class, args);
    }
	
}
