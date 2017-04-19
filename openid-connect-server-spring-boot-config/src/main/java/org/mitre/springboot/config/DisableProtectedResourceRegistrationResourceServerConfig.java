package org.mitre.springboot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 *
 * This configuration is necessary because the application doesn´t work if the protectedresourcesregistration endpoint is disabled. 
 * 
 */
@Configuration
@ConditionalOnProperty(havingValue = "false", name = "openid.connect.endpoints.protectedresourceregistration.enabled", matchIfMissing = true)
@Order(210)
public class DisableProtectedResourceRegistrationResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Override
    public void configure(final HttpSecurity http) throws Exception{
        // @formatter:off
		http
		    .sessionManagement()
		        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		    .and()
			.authorizeRequests()
				.anyRequest().authenticated();
		// @formatter:on
    }

}