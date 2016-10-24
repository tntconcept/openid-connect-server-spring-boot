package org.mitre.springboot.config.ui;

import org.mitre.openid.connect.web.AuthenticationTimeStamper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Order(600)
@Configuration
public class UserLoginConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationTimeStamper authenticationTimeStamper;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.sessionManagement()
				.enableSessionUrlRewriting(false)
				.and()
			.csrf()
				.and()
			.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error=failure")
				.successHandler(authenticationTimeStamper)
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/**")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/login?logout")
				//TODO upgrade default logout UI this should be a POST with CSRF for safety
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				.permitAll()
				.and()
			.anonymous()
				.and()
			.headers()
			 	.frameOptions().deny()

		;
		// @formatter:on

	}

}
