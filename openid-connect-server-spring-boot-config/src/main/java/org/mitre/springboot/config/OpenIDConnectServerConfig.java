package org.mitre.springboot.config;

import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.mitre.springboot.config.annotation.EnableOpenIDConnectServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@ComponentScan(basePackages = {"org.mitre", "org.mitre.springboot.config"}) 
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
	
}
