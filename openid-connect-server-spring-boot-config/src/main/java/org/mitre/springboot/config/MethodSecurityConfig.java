package org.mitre.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/*
	<security:global-method-security pre-post-annotations="enabled" proxy-target-class="true" authentication-manager-ref="authenticationManager">
		<!--you could also wire in the expression handler up at the layer of the http filters. See https://jira.springsource.org/browse/SEC-1452 -->
		<security:expression-handler ref="oauthExpressionHandler" />
	</security:global-method-security>
	<oauth:expression-handler id="oauthExpressionHandler" />
*/
//Currently and issue with this running with embedded tomcat http://stackoverflow.com/questions/24598959/spring-boot-security-cannot-customize-security-when-running-on-embedded-tomc
//https://github.com/spring-projects/spring-security/issues/2950
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
//@Order(1)
public class MethodSecurityConfig { // extends GlobalMethodSecurityConfiguration {
	
	/* This is now handled in OAuth2MethodSecurityConfiguration
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}

	// uses the global authentication manager, todo this should be declared better as this qualifier is the one from the
	// xml config

	@Autowired
	@Qualifier("org.springframework.security.authenticationManager")
	private AuthenticationManager gloablAuthManager;

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return gloablAuthManager;
	}
	*/
}
