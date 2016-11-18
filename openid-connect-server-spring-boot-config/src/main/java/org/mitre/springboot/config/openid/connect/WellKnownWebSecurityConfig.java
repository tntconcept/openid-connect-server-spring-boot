package org.mitre.springboot.config.openid.connect;

import org.mitre.discovery.view.WebfingerView;
import org.mitre.discovery.web.DiscoveryEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Order(160)
@Configuration
@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oidc.discovery.enabled", matchIfMissing=true)
public class WellKnownWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	protected Http403ForbiddenEntryPoint http403ForbiddenEntryPoint;
	
	@Bean
	@ConditionalOnMissingBean(DiscoveryEndpoint.class)
	protected DiscoveryEndpoint discoveryEndpoint()  {
		return new DiscoveryEndpoint();
	}
	
	@Bean(name="webfingerView")
	@ConditionalOnMissingBean(name="webfingerView")
	protected WebfingerView webfingerView()  {
		return new WebfingerView();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.requestMatchers()
				.antMatchers("/.well-known/**")
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(http403ForbiddenEntryPoint)
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
		.authorizeRequests()
			.antMatchers("/.well-known/**")
			.permitAll()
		;
		// @formatter:on
	}
}	
