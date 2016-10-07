package org.mitre.springboot.config;

import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.mitre.springboot.config.annotation.EnableOpenIDConnectServer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConditionalOnClass(EnableOpenIDConnectServer.class)
@ComponentScan(basePackages = {"org.mitre", "org.mitre.config"}) 
@EnableTransactionManagement
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableWebSecurity
@EnableResourceServer
@EnableAuthorizationServer
@EnableConfigurationProperties()
public class OpenIDConnectServerConfig {

	@Bean(name="config")
	@ConfigurationProperties(prefix = "openid.connect.server")
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
	
}
