package org.mitre.springboot.config;

import java.util.Arrays;

import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.oauth2.service.OAuth2TokenEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
/**
 * Wires in the MitreID OpenID Connect implementations into the Spring Security Oauth 2.0 stack, overrides OAuth2AuthorizationServerConfiguration
 * 
 */

@Configuration
@Order(500)
public class OpenIDConnectAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private ClientDetailsEntityService clientDetailsService;
	
	@Autowired
	@Qualifier("defaultOAuth2ProviderTokenService")
	private OAuth2TokenEntityService tokenServices;
	
	@Autowired
	@Qualifier("tofuUserApprovalHandler")
	private UserApprovalHandler userApprovalHandler;
	
	@Autowired
	@Qualifier("connectOAuth2RequestFactory")
	private OAuth2RequestFactory requestFactory;
	
	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;
	
	@Autowired
	@Qualifier("chainedTokenGranter")
	private TokenGranter chainedTokenGranter;
	
	@Autowired
	private WebResponseExceptionTranslator exceptionTranslator;
	
	@Autowired 
	@Qualifier("jwtAssertionTokenGranter")
	private TokenGranter jwtAssertionTokenGranter;
	
	@Autowired 
	private OAuth2RequestValidator oAuth2RequestValidator;
	
	@Autowired
	@Qualifier("clientPasswordEncoder")
	private PasswordEncoder clientPasswordEncoder;
	
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
			.requestValidator(oAuth2RequestValidator)
			.pathMapping("/oauth/token", "/token")
			.pathMapping("/oauth/authorize", "/authorize")
			.tokenServices(tokenServices)
			.userApprovalHandler(userApprovalHandler)
			.requestFactory(requestFactory)
			.exceptionTranslator(exceptionTranslator)
			.tokenGranter(tokenGranter())
			.authorizationCodeServices(authorizationCodeServices)
		;
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	    oauthServer.passwordEncoder(clientPasswordEncoder);
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService);
	}
	
	
}
