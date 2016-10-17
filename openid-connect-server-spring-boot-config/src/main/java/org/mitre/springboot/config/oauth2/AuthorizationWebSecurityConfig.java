package org.mitre.springboot.config.oauth2;

import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Order(710)
@Configuration
public class AuthorizationWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthorizationRequestFilter authRequestFilter ;

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
