package org.mitre.springboot.config;

import java.util.Arrays;

import org.mitre.oauth2.service.impl.DefaultOAuth2ClientDetailsEntityService;
import org.mitre.oauth2.service.impl.DefaultOAuth2ProviderTokenService;
import org.mitre.oauth2.token.ChainedTokenGranter;
import org.mitre.oauth2.token.JWTAssertionTokenGranter;
import org.mitre.oauth2.token.StructuredScopeAwareOAuth2RequestValidator;
import org.mitre.openid.connect.request.ConnectOAuth2RequestFactory;
import org.mitre.openid.connect.token.TofuUserApprovalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
/**
 * Wires in the MitreID OpenID Connect implementations into the Spring Security Oauth 2.0 stack, overrides OAuth2AuthorizationServerConfiguration
 * 
 */
@Configuration
//@ConditionalOnMissingBean(AuthorizationServerConfigurer.class)
@Order(200)
public class OpenIDConnectAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private DefaultOAuth2ClientDetailsEntityService clientDetailsService;
	
	@Autowired
	private DefaultOAuth2ProviderTokenService tokenServices;
	
	@Autowired
	private TofuUserApprovalHandler tofuUserApprovalHandler;
	
	@Autowired
	private ConnectOAuth2RequestFactory requestFactory;
	
	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;
	
	@Autowired
	private ChainedTokenGranter chainedTokenGranter;
	
	@Autowired
	private WebResponseExceptionTranslator exceptionTranslator;
	
	@Autowired 
	private JWTAssertionTokenGranter jwtAssertionTokenGranter;
	
	@Bean
	protected OAuth2RequestValidator requestValidator() {
		return new StructuredScopeAwareOAuth2RequestValidator();
	}
	
	@Bean
	protected OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler(){
		return new OAuth2AccessDeniedHandler();
	}
	
	protected TokenGranter tokenGranter() {
		return new CompositeTokenGranter(Arrays.<TokenGranter>asList(
				new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory),
				new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory),
				new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory),
				new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory),
				chainedTokenGranter,
				jwtAssertionTokenGranter
				));
	}
	
	//TODO Set this on AuthorizationEndpoint.setRedirectResolver()
	//redirect-resolver-ref="blacklistAwareRedirectResolver"
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//TODO
		endpoints
			.requestValidator(requestValidator())
			.pathMapping("/oauth/token", "/token")
			.pathMapping("/oauth/authorize", "/authorize")
			.tokenServices(tokenServices)
			.userApprovalHandler(tofuUserApprovalHandler)
			.requestFactory(requestFactory)
			.exceptionTranslator(exceptionTranslator)
			.tokenGranter(tokenGranter())
			.authorizationCodeServices(authorizationCodeServices)
			
		;
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService);
	}
	
	
}
