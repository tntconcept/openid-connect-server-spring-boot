package org.mitre.springboot.config;

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
import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
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
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
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
"org.mitre.oauth2.service.impl",
"org.mitre.oauth2.token",
"org.mitre.oauth2.view",
"org.mitre.oauth2.web",
//"org.mitre.openid.connect.assertion",
//"org.mitre.openid.connect.config",
//"org.mitre.openid.connect.exception",
"org.mitre.openid.connect.filter",
"org.mitre.openid.connect.repository.impl",
"org.mitre.openid.connect.request",
"org.mitre.openid.connect.service.impl",
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

	
	/*
	 * Specific configuration for the package "org.mitre.openid.connect.web" 
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
	 * Specific configuration for "org.mitre.discovery.view","org.mitre.discovery.web"
	 */
	@Configuration
	@ConditionalOnProperty(havingValue="true", name="openid.connect.endpoints.oidc.discovery.enabled", matchIfMissing=true)
	@Import(value={WebfingerView.class, DiscoveryEndpoint.class})
	public static class DiscoveryEndpointConfiguration {}
	
	/*
	 * Specific configuration for "org.mitre.oauth2.repository.impl"
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
	 * Specific configuration for "org.mitre.oauth2.service.impl"
	 */
	
	
	
}
