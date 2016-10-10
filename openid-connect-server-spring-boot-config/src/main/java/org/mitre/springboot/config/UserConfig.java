package org.mitre.springboot.config;

import javax.sql.DataSource;

import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.mitre.openid.connect.web.AuthenticationTimeStamper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Order(600)
@Configuration
public class UserConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationTimeStamper authenticationTimeStamper;
	
	@Autowired // This should be created by ResourceServerSecurityConfigurer
	private OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler;
	
	@Autowired
	//private PromptFilter promptFilter;
	private AuthorizationRequestFilter authRequestFilter ;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource);
	}

	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.expressionHandler(oAuth2WebSecurityExpressionHandler);
	}
	
	/* TODO Java config for
	 * <security:headers>
	<security:frame-options policy="DENY" />
</security:headers>
<security:intercept-url pattern="/authorize" access="hasRole('ROLE_USER')" />
	 */
	
	
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
			.authorizeRequests()
				.antMatchers("/authorize")
				.hasRole("USER")
				.and()
			.addFilterAfter(authRequestFilter, SecurityContextPersistenceFilter.class)
			.logout()
				.logoutSuccessUrl("/login?logout")
				//TODO upgrade default logout UI this should be a POST with CSRF for safety
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				.permitAll()
				.and()
			.anonymous()
				.and()
			//.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/authorize")).disable()
		;
		// @formatter:on

	}

	@Bean(name = "org.springframework.security.authenticationManager")
	public AuthenticationManager authenticationManagerBean()
			throws Exception {
		return super.authenticationManagerBean();
	}

}
