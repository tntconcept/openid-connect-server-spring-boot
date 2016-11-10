package org.mitre.springboot.config.oauth2;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Order(710)
@Configuration
public class AuthorizationWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("authRequestFilter")
	private Filter authRequestFilter ;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.authorizeRequests()
				.antMatchers("/authorize")
				.hasRole("USER")
				.and()
			.addFilterAfter(authRequestFilter, SecurityContextPersistenceFilter.class)
		;
		// @formatter:on
	}
	
}
