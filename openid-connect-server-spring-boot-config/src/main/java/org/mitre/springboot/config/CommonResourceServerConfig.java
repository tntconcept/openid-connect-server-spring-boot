	package org.mitre.springboot.config;

import org.mitre.oauth2.service.impl.DefaultOAuth2ProviderTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

@Configuration
@Order(170)
public class CommonResourceServerConfig extends ResourceServerConfigurerAdapter { 

	@Autowired
	private DefaultOAuth2ProviderTokenService defaultOAuth2ProviderTokenService;
	
	@Bean(name="oauthAuthenticationEntryPoint") //named only for transition from XML, remove name later
	public OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setRealmName("openidconnect");
		return entryPoint;
	}

	@Bean
	public OAuth2WebSecurityExpressionHandler oauthWebExpressionHandler() {
		return new OAuth2WebSecurityExpressionHandler();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(false);
		resources.tokenServices(defaultOAuth2ProviderTokenService);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
	}

	
}
