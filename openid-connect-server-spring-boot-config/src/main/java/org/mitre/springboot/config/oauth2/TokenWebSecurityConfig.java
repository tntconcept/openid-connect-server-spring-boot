package org.mitre.springboot.config.oauth2;

import java.util.Collections;
import java.util.HashSet;

import org.mitre.oauth2.web.CorsFilter;
import org.mitre.openid.connect.assertion.JWTBearerAuthenticationProvider;
import org.mitre.openid.connect.assertion.JWTBearerClientAssertionTokenEndpointFilter;
import org.mitre.openid.connect.filter.MultiUrlRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@Order(101)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	protected CorsFilter corsFilter;
	
	@Autowired
	protected OAuth2AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	@Qualifier("clientUserDetailsService")
	protected UserDetailsService clientUserDetailsService;
	
	@Autowired
	@Qualifier("uriEncodedClientUserDetailsService")
	protected UserDetailsService uriEncodedClientUserDetailsService;
	
	@Autowired
	protected OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(clientUserDetailsService);
		auth.userDetailsService(uriEncodedClientUserDetailsService);
	}

	
	@Bean
	public ClientCredentialsTokenEndpointFilter clientCredentialsEndpointFilter() throws Exception {
		ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter();
		filter.setRequiresAuthenticationRequestMatcher(clientAuthMatcher());
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}
	
	@Bean
	public JWTBearerClientAssertionTokenEndpointFilter clientAssertionEndpointFilter( ) {
		JWTBearerClientAssertionTokenEndpointFilter filter = new JWTBearerClientAssertionTokenEndpointFilter(clientAuthMatcher());	
		filter.setAuthenticationManager(new ProviderManager(Collections.<AuthenticationProvider>singletonList(new JWTBearerAuthenticationProvider())));
		return filter;
	}
	
	//TODO allow override above
	@Bean
	public JWTBearerAuthenticationProvider jwtBearerAuthenticationProvider() {
		return new JWTBearerAuthenticationProvider();
	}
	
	@Bean
	public MultiUrlRequestMatcher clientAuthMatcher() {
		HashSet<String> urls = new HashSet<String>();
		urls.add("/introspect");
		urls.add("/revoke");
		urls.add("/token");
		return new MultiUrlRequestMatcher(urls);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.requestMatchers()
				.antMatchers("/token")
				.and()
			.httpBasic()
				.authenticationEntryPoint(authenticationEntryPoint)
				.and()
			.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/token").permitAll()
				.antMatchers("/token").authenticated()
				.and()
			.addFilterAfter(clientAssertionEndpointFilter(), AbstractPreAuthenticatedProcessingFilter.class)
			.addFilterAfter(clientCredentialsEndpointFilter(), BasicAuthenticationFilter.class)	
			.addFilterAfter(corsFilter, SecurityContextPersistenceFilter.class)
			
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(oAuth2AccessDeniedHandler)
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		;
		// @formatter:on
	}
}
