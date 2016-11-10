package org.mitre.springboot.config;

import javax.servlet.Filter;

import org.mitre.discovery.view.WebfingerView;
import org.mitre.discovery.web.DiscoveryEndpoint;
import org.mitre.jwt.signer.service.impl.ClientKeyCacheService;
import org.mitre.jwt.signer.service.impl.JWKSetCacheService;
import org.mitre.jwt.signer.service.impl.SymmetricKeyJWTValidatorCacheService;
import org.mitre.oauth2.repository.AuthenticationHolderRepository;
import org.mitre.oauth2.repository.AuthorizationCodeRepository;
import org.mitre.oauth2.repository.OAuth2ClientRepository;
import org.mitre.oauth2.repository.OAuth2TokenRepository;
import org.mitre.oauth2.repository.SystemScopeRepository;
import org.mitre.oauth2.repository.impl.JpaAuthenticationHolderRepository;
import org.mitre.oauth2.repository.impl.JpaAuthorizationCodeRepository;
import org.mitre.oauth2.repository.impl.JpaOAuth2ClientRepository;
import org.mitre.oauth2.repository.impl.JpaOAuth2TokenRepository;
import org.mitre.oauth2.repository.impl.JpaSystemScopeRepository;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.oauth2.service.IntrospectionResultAssembler;
import org.mitre.oauth2.service.OAuth2TokenEntityService;
import org.mitre.oauth2.service.SystemScopeService;
import org.mitre.oauth2.service.impl.BlacklistAwareRedirectResolver;
import org.mitre.oauth2.service.impl.DefaultClientUserDetailsService;
import org.mitre.oauth2.service.impl.DefaultIntrospectionResultAssembler;
import org.mitre.oauth2.service.impl.DefaultOAuth2AuthorizationCodeService;
import org.mitre.oauth2.service.impl.DefaultOAuth2ClientDetailsEntityService;
import org.mitre.oauth2.service.impl.DefaultOAuth2ProviderTokenService;
import org.mitre.oauth2.service.impl.DefaultSystemScopeService;
import org.mitre.oauth2.service.impl.UriEncodedClientUserDetailsService;
import org.mitre.oauth2.token.ChainedTokenGranter;
import org.mitre.oauth2.token.JWTAssertionTokenGranter;
import org.mitre.oauth2.token.StructuredScopeAwareOAuth2RequestValidator;
import org.mitre.oauth2.view.TokenApiView;
import org.mitre.oauth2.web.CorsFilter;
import org.mitre.oauth2.web.IntrospectionEndpoint;
import org.mitre.oauth2.web.OAuthConfirmationController;
import org.mitre.oauth2.web.RevocationEndpoint;
import org.mitre.oauth2.web.ScopeAPI;
import org.mitre.oauth2.web.TokenAPI;
import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.mitre.openid.connect.filter.AuthorizationRequestFilter;
import org.mitre.openid.connect.repository.AddressRepository;
import org.mitre.openid.connect.repository.ApprovedSiteRepository;
import org.mitre.openid.connect.repository.BlacklistedSiteRepository;
import org.mitre.openid.connect.repository.PairwiseIdentifierRepository;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.mitre.openid.connect.repository.WhitelistedSiteRepository;
import org.mitre.openid.connect.repository.impl.JpaAddressRepository;
import org.mitre.openid.connect.repository.impl.JpaApprovedSiteRepository;
import org.mitre.openid.connect.repository.impl.JpaBlacklistedSiteRepository;
import org.mitre.openid.connect.repository.impl.JpaPairwiseIdentifierRepository;
import org.mitre.openid.connect.repository.impl.JpaUserInfoRepository;
import org.mitre.openid.connect.repository.impl.JpaWhitelistedSiteRepository;
import org.mitre.openid.connect.request.ConnectOAuth2RequestFactory;
import org.mitre.openid.connect.service.ApprovedSiteService;
import org.mitre.openid.connect.service.BlacklistedSiteService;
import org.mitre.openid.connect.service.OIDCTokenService;
import org.mitre.openid.connect.service.PairwiseIdentiferService;
import org.mitre.openid.connect.service.ScopeClaimTranslationService;
import org.mitre.openid.connect.service.StatsService;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.service.WhitelistedSiteService;
import org.mitre.openid.connect.service.impl.DefaultApprovedSiteService;
import org.mitre.openid.connect.service.impl.DefaultBlacklistedSiteService;
import org.mitre.openid.connect.service.impl.DefaultOIDCTokenService;
import org.mitre.openid.connect.service.impl.DefaultScopeClaimTranslationService;
import org.mitre.openid.connect.service.impl.DefaultStatsService;
import org.mitre.openid.connect.service.impl.DefaultUserInfoService;
import org.mitre.openid.connect.service.impl.DefaultWhitelistedSiteService;
import org.mitre.openid.connect.service.impl.DummyResourceSetService;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_0;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_1;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_2;
import org.mitre.openid.connect.service.impl.UUIDPairwiseIdentiferService;
import org.mitre.openid.connect.web.ApprovedSiteAPI;
import org.mitre.openid.connect.web.AuthenticationTimeStamper;
import org.mitre.openid.connect.web.BlacklistAPI;
import org.mitre.openid.connect.web.ClientAPI;
import org.mitre.openid.connect.web.DataAPI;
import org.mitre.openid.connect.web.DynamicClientRegistrationEndpoint;
import org.mitre.openid.connect.web.JWKSetPublishingEndpoint;
import org.mitre.openid.connect.web.ProtectedResourceRegistrationEndpoint;
import org.mitre.openid.connect.web.StatsAPI;
import org.mitre.openid.connect.web.UserInfoEndpoint;
import org.mitre.openid.connect.web.WhitelistAPI;
import org.mitre.springboot.config.annotation.EnableOpenIDConnectServer;
import org.mitre.uma.service.ResourceSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.servlet.view.BeanNameViewResolver;

@Configuration
@ConditionalOnClass(EnableOpenIDConnectServer.class)
@ComponentScan(basePackages = {
		
//"org.mitre", 

//Server packages
//"org.mitre.discovery.view",
//"org.mitre.discovery.web",
//"org.mitre.oauth2.repository.impl",
//"org.mitre.oauth2.service.impl",
//"org.mitre.oauth2.token",
//"org.mitre.oauth2.view",
//"org.mitre.oauth2.web",
//"org.mitre.openid.connect.assertion",
//"org.mitre.openid.connect.config",
//"org.mitre.openid.connect.exception",
//"org.mitre.openid.connect.filter",
//"org.mitre.openid.connect.repository.impl",
//"org.mitre.openid.connect.request",
//"org.mitre.openid.connect.service.impl",
"org.mitre.openid.connect.token",
//"org.mitre.openid.connect.util",
"org.mitre.openid.connect.view",
//"org.mitre.openid.connect.web",

//Commons packages
//"org.mitre.discovery.util",
//"org.mitre.jose.keystore",
//"org.mitre.jwt.encryption.service",
//"org.mitre.jwt.encryption.service.impl",
//"org.mitre.jwt.signer.service",
//"org.mitre.jwt.signer.service.impl",
//"org.mitre.oauth2.model",
//"org.mitre.oauth2.model.convert",
//"org.mitre.oauth2.repository",
//"org.mitre.oauth2.service",
//"org.mitre.openid.connect",
//"org.mitre.openid.connect.model",
//"org.mitre.openid.connect.model.convert",
//"org.mitre.openid.connect.repository",
//"org.mitre.openid.connect.service",
//"org.mitre.uma.model",
//"org.mitre.uma.model.convert",
//"org.mitre.uma.repository",
//"org.mitre.uma.service",
//"org.mitre.util",
//"org.mitre.util.jpa",

//Spring boot packages
"org.mitre.springboot.config"}
)
@EnableWebSecurity
@EnableResourceServer
@EnableAuthorizationServer
@EnableConfigurationProperties()
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@Order(101)
public class OpenIDConnectServerConfig {

	@Bean(name="config")
	@ConfigurationProperties(prefix = "openid.connect.server")
	@ConditionalOnMissingBean(ConfigurationPropertiesBean.class)
	public ConfigurationPropertiesBean configurationPropertiesBean() {
    	return new ConfigurationPropertiesBean();
	}
	
    @Bean
	public Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
		return new Http403ForbiddenEntryPoint();
	}
	
	@Bean
	public WebResponseExceptionTranslator defaultWebResponseExceptionTranslator() {
		return new DefaultWebResponseExceptionTranslator();
	}
	
	@Bean
	public OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setRealmName("openidconnect");
		return entryPoint;
	}

	@Bean
	public OAuth2WebSecurityExpressionHandler oauthWebExpressionHandler() {
		return new OAuth2WebSecurityExpressionHandler();
	}
	
	/*
	 * This is to allow resolution of the MitreID json endpoint view beans used in API and OIDC endpoints
	 */
	@Bean
    public BeanNameViewResolver beanViewResolver() {
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        resolver.setOrder(0);
        return resolver;
    }

	@Bean
	@ConditionalOnMissingBean(OAuth2AccessDeniedHandler.class)
	protected OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler(){
		return new OAuth2AccessDeniedHandler();
	}
	
	/*
	 * Enabled configuration for the package "org.mitre.openid.connect.web" 
	 */
	@Configuration
	@Import(value=AuthenticationTimeStamper.class)
	public static class WebEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.whitelist.enabled", matchIfMissing=true)
	@Import(value=WhitelistAPI.class)
	public static class WhitelistEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.approvedsite.enabled", matchIfMissing=true)
	@Import(value=ApprovedSiteAPI.class)
	public static class ApprovedSiteEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.blacklist.enabled", matchIfMissing=true)
	@Import(value=BlacklistAPI.class)
	public static class BlacklistEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.client.enabled", matchIfMissing=true)
	@Import(value=ClientAPI.class)
	public static class ClientEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.data.enabled", matchIfMissing=true)
	@Import(value=DataAPI.class)
	public static class DataEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oidc.dynamicclientregistration.enabled", matchIfMissing=true)
	@Import(value=DynamicClientRegistrationEndpoint.class)
	public static class DynamicClientRegistrationEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oidc.jwksetpublishing.enabled", matchIfMissing=true)
	@Import(value=JWKSetPublishingEndpoint.class)
	public static class JWKsetPublishingEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oidc.userinfo.enabled", matchIfMissing=true)
	@Import(value=UserInfoEndpoint.class)
	public static class UserInfoEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.protectedresourceregistration.enabled", matchIfMissing=true)
	@Import(value=ProtectedResourceRegistrationEndpoint.class)
	public static class ProtectedResourceRegistrationEndpointConfiguration {}

	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.stats.enabled", matchIfMissing=true)
	@Import(value=StatsAPI.class)
	public static class StatsEndpointConfiguration {}
	
	/*
	 * Specific configuration for "org.mitre.jwt.signer.service.impl"
	 */
	
	@Configuration
	@Import(value={ClientKeyCacheService.class, JWKSetCacheService.class, SymmetricKeyJWTValidatorCacheService.class})
	public static class JwtSignerServiceConfiguration {}
	
	/*
	 * Enabled configuration for "org.mitre.discovery.view","org.mitre.discovery.web"
	 */
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oidc.discovery.enabled", matchIfMissing=true)
	@Import(value={WebfingerView.class, DiscoveryEndpoint.class})
	public static class DiscoveryEndpointConfiguration {}
	
	/*
	 * Override configuration for "org.mitre.oauth2.repository.impl"
	 */
	
	@Bean
	@ConditionalOnMissingBean(AuthenticationHolderRepository.class)
	public AuthenticationHolderRepository jpaAuthenticationHolderRepository() {
		return new JpaAuthenticationHolderRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(AuthorizationCodeRepository.class)
	public AuthorizationCodeRepository jpaAuthorizationCodeRepository() {
		return new JpaAuthorizationCodeRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(OAuth2ClientRepository.class)
	public OAuth2ClientRepository jpaOAuth2ClientRepository() {
		return new JpaOAuth2ClientRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(OAuth2TokenRepository.class)
	public OAuth2TokenRepository jpaOAuth2TokenRepository() {
		return new JpaOAuth2TokenRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(SystemScopeRepository.class)
	public SystemScopeRepository jpaSystemScopeRepository() {
		return new JpaSystemScopeRepository();
	}
	
	/*
	 * Override configuration for "org.mitre.oauth2.service.impl"
	 */
	@Bean
	@ConditionalOnMissingBean(BlacklistAwareRedirectResolver.class)
	public BlacklistAwareRedirectResolver blacklistAwareRedirectResolver() {
		return new BlacklistAwareRedirectResolver();
	}
	
	@Bean
	@ConditionalOnMissingBean(IntrospectionResultAssembler.class)
	public IntrospectionResultAssembler defaultIntrospectionResultAssembler() {
		return new DefaultIntrospectionResultAssembler();
	}
	
	@Bean
	@ConditionalOnMissingBean(AuthorizationCodeServices.class)
	public AuthorizationCodeServices defaultOAuth2AuthorizationCodeService() {
		return new DefaultOAuth2AuthorizationCodeService();
	}
	
	@Bean
	@ConditionalOnMissingBean(ClientDetailsEntityService.class)
	public ClientDetailsEntityService defaultOAuth2ClientDetailsEntityService() {
		return new DefaultOAuth2ClientDetailsEntityService();
	}
	
	@Bean
	@ConditionalOnMissingBean(OAuth2TokenEntityService.class)
	public OAuth2TokenEntityService defaultOAuth2ProviderTokenService() {
		return new DefaultOAuth2ProviderTokenService();
	}
	
	@Bean
	@ConditionalOnMissingBean(SystemScopeService.class)
	public SystemScopeService defaultSystemScopeService() {
		return new DefaultSystemScopeService();
	}
	
	@Bean
	@ConditionalOnMissingBean(name="clientUserDetailsService")
	public UserDetailsService clientUserDetailsService() {
		return new DefaultClientUserDetailsService();
	}
	
	@Bean
	@ConditionalOnMissingBean(name="uriEncodedClientUserDetailsService")
	public UserDetailsService uriEncodedClientUserDetailsService() {
		return new UriEncodedClientUserDetailsService();
	}
	
	/*
	 * Override configuration for "org.mitre.oauth2.token"
	 */
	@Bean
	@Autowired
	@ConditionalOnMissingBean(name="chainedTokenGranter")
	public TokenGranter chainedTokenGranter(OAuth2TokenEntityService tokenServices, ClientDetailsEntityService clientDetailsService, OAuth2RequestFactory requestFactory) {
		return new ChainedTokenGranter(tokenServices, clientDetailsService, requestFactory);
	}
	
	@Bean
	@Autowired
	@ConditionalOnMissingBean(name="jwtAssertionTokenGranter")
	public TokenGranter jwtAssertionTokenGranter(OAuth2TokenEntityService tokenServices, ClientDetailsEntityService clientDetailsService, OAuth2RequestFactory requestFactory) {
		return new JWTAssertionTokenGranter(tokenServices, clientDetailsService, requestFactory);
	}
	
	@Bean
	@ConditionalOnMissingBean(OAuth2RequestValidator.class)
	protected OAuth2RequestValidator requestValidator() {
		return new StructuredScopeAwareOAuth2RequestValidator();
	}
	
	/*
	 * Endpoint configuration for "org.mitre.oauth2.view", "org.mitre.oauth2.web",
	 */
	
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.token.enabled", matchIfMissing=true)
	@Import(value={TokenApiView.class, TokenAPI.class})
	public static class TokenAPIConfiguration {}
	
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.api.scope.enabled", matchIfMissing=true)
	@Import(value={ScopeAPI.class})
	public static class ScopeAPIConfiguration {}
	
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oauth2.introspection.enabled", matchIfMissing=true)
	@Import(value={IntrospectionEndpoint.class})
	public static class IntrospectionEndpointConfiguration {}
	
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oauth2.confirmation.enabled", matchIfMissing=true)
	@Import(value={OAuthConfirmationController.class})
	public static class OAuthConfirmationControllerConfiguration {}
	
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oauth2.revocation.enabled", matchIfMissing=true)
	@Import(value={RevocationEndpoint.class})
	public static class RevocationEndpointConfiguration {}
	
	@Bean
	@ConditionalOnMissingBean(name="corsFilter")
	public Filter corsFilter() {
		return new CorsFilter();
	}
	
	/*
	 * Override configuration for "org.mitre.openid.connect.filter"
	 */
	@Bean
	@Autowired
	@ConditionalOnMissingBean(name="authRequestFilter")
	public Filter authRequestFilter() {
		return new AuthorizationRequestFilter();
	}
	
	/*
	 * Override configuration "org.mitre.openid.connect.repository.impl"
	 */
	
	@Bean
	@ConditionalOnMissingBean(AddressRepository.class)
	public AddressRepository jpaAddressRepository() {
		return new JpaAddressRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(ApprovedSiteRepository.class)
	public ApprovedSiteRepository jpaApprovedSiteRepository() {
		return new JpaApprovedSiteRepository();
	}
	
	
	@Bean
	@ConditionalOnMissingBean(BlacklistedSiteRepository.class)
	public BlacklistedSiteRepository jpaBlacklistedSiteRepository() {
		return new JpaBlacklistedSiteRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(PairwiseIdentifierRepository.class)
	public PairwiseIdentifierRepository jpaPairwiseIdentifierRepository() {
		return new JpaPairwiseIdentifierRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(UserInfoRepository.class)
	public UserInfoRepository jpaUserInfoRepository() {
		return new JpaUserInfoRepository();
	}
	
	@Bean
	@ConditionalOnMissingBean(WhitelistedSiteRepository.class)
	public WhitelistedSiteRepository jpaWhitelistedSiteRepository() {
		return new JpaWhitelistedSiteRepository();
	}
	
	/*
	 * Override configuration "org.mitre.openid.connect.request"
	 */
	
	@Bean
	@Autowired
	@ConditionalOnMissingBean(name="connectOAuth2RequestFactory")
	public OAuth2RequestFactory connectOAuth2RequestFactory(ClientDetailsEntityService clientDetailsService) {
		return new ConnectOAuth2RequestFactory(clientDetailsService);
	}
	
	/*
	 * Override configuration "org.mitre.openid.connect.service.impl"
	 */
	
	@Bean
	@ConditionalOnMissingBean(ApprovedSiteService.class)
	public ApprovedSiteService defaultApprovedSiteService() {
		return new DefaultApprovedSiteService();
	}
	
	@Bean
	@ConditionalOnMissingBean(BlacklistedSiteService.class)
	public BlacklistedSiteService defaultBlacklistedSiteService() {
		return new DefaultBlacklistedSiteService();
	}
	
	@Bean
	@ConditionalOnMissingBean(OIDCTokenService.class)
	public OIDCTokenService defaultOIDCTokenService() {
		return new DefaultOIDCTokenService();
	}
	
	@Bean
	@ConditionalOnMissingBean(ScopeClaimTranslationService.class)
	public ScopeClaimTranslationService scopeClaimTranslator() {
		return new DefaultScopeClaimTranslationService();
	}
	
	@Bean
	@ConditionalOnMissingBean(StatsService.class)
	public StatsService defaultStatsService() {
		return new DefaultStatsService();
	}
	
	@Bean
	@ConditionalOnMissingBean(UserInfoService.class)
	public UserInfoService defaultUserInfoService() {
		return new DefaultUserInfoService();
	}
	
	@Bean
	@ConditionalOnMissingBean(WhitelistedSiteService.class)
	public WhitelistedSiteService defaultWhitelistedSiteService() {
		return new DefaultWhitelistedSiteService();
	}
	
	@Bean
	@ConditionalOnMissingBean(ResourceSetService.class)
	public ResourceSetService dummyResourceSetService() {
		return new DummyResourceSetService();
	}
	
	@Bean
	@ConditionalOnMissingBean(MITREidDataService_1_0.class)
	public MITREidDataService_1_0 MITREidDataService_1_0() {
		return new MITREidDataService_1_0();
	}
	
	@Bean
	@ConditionalOnMissingBean(MITREidDataService_1_1.class)
	public MITREidDataService_1_1 MITREidDataService_1_1() {
		return new MITREidDataService_1_1();
	}

	@Bean
	@ConditionalOnMissingBean(MITREidDataService_1_2.class)
	public MITREidDataService_1_2 MITREidDataService_1_2() {
		return new MITREidDataService_1_2();
	}
	
	@Bean
	@ConditionalOnMissingBean(PairwiseIdentiferService.class)
	public PairwiseIdentiferService uuidPairwiseIdentiferService() {
		return new UUIDPairwiseIdentiferService();
	}
	
	
}
