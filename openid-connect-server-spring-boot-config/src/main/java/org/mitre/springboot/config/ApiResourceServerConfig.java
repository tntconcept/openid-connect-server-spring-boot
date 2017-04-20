package org.mitre.springboot.config;

import org.mitre.oauth2.view.TokenApiView;
import org.mitre.oauth2.web.ScopeAPI;
import org.mitre.oauth2.web.TokenAPI;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_0;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_1;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_2;
import org.mitre.openid.connect.service.impl.MITREidDataService_1_3;
import org.mitre.openid.connect.view.ClientEntityViewForAdmins;
import org.mitre.openid.connect.view.ClientEntityViewForUsers;
import org.mitre.openid.connect.view.JsonApprovedSiteView;
import org.mitre.openid.connect.web.ApprovedSiteAPI;
import org.mitre.openid.connect.web.BlacklistAPI;
import org.mitre.openid.connect.web.ClientAPI;
import org.mitre.openid.connect.web.DataAPI;
import org.mitre.openid.connect.web.StatsAPI;
import org.mitre.openid.connect.web.WhitelistAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

public abstract class ApiResourceServerConfig extends ResourceServerConfigurerAdapter{

    protected abstract String getPattern();

    @Autowired
    private OAuth2AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(final HttpSecurity http) throws Exception{
        // @formatter:off
		http
			.requestMatchers()
				.antMatchers("/" + getPattern() + "/**")
				.and()
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			
		;
		// @formatter:on
    }

    @Order(180)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.whitelist.enabled", matchIfMissing = true)
    @Import(value = WhitelistAPI.class)
    public static class WhitelistEndpointConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return WhitelistAPI.URL;
        }
    }

    @Order(181)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.approvedsite.enabled", matchIfMissing = true)
    @Import(value = {ApprovedSiteAPI.class, JsonApprovedSiteView.class})
    public static class ApprovedSiteEndpointConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return ApprovedSiteAPI.URL;
        }
    }

    @Order(182)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.blacklist.enabled", matchIfMissing = true)
    @Import(value = BlacklistAPI.class)
    public static class BlacklistEndpointConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return BlacklistAPI.URL;
        }
    }

    @Order(183)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.client.enabled", matchIfMissing = true)
    @Import(value = {ClientAPI.class, ClientEntityViewForAdmins.class, ClientEntityViewForUsers.class})
    public static class ClientEndpointConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return ClientAPI.URL;
        }
    }

    @Order(184)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.data.enabled", matchIfMissing = true)
    @Import(value = DataAPI.class)
    public static class DataEndpointConfiguration extends ApiResourceServerConfig{

        @Override
        protected String getPattern(){
            return DataAPI.URL;
        }

        @Bean
        @ConditionalOnMissingBean(MITREidDataService_1_0.class)
        public MITREidDataService_1_0 MITREidDataService_1_0(){
            return new MITREidDataService_1_0();
        }

        @Bean
        @ConditionalOnMissingBean(MITREidDataService_1_1.class)
        public MITREidDataService_1_1 MITREidDataService_1_1(){
            return new MITREidDataService_1_1();
        }

        @Bean
        @ConditionalOnMissingBean(MITREidDataService_1_2.class)
        public MITREidDataService_1_2 MITREidDataService_1_2(){
            return new MITREidDataService_1_2();
        }

        @Bean
        @ConditionalOnMissingBean(MITREidDataService_1_3.class)
        public MITREidDataService_1_3 MITREidDataService_1_3(){
            return new MITREidDataService_1_3();
        }

    }

    @Order(185)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.stats.enabled", matchIfMissing = true)
    @Import(value = StatsAPI.class)
    public static class StatsEndpointConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return StatsAPI.URL;
        }
    }

    @Order(186)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.token.enabled", matchIfMissing = true)
    @Import(value = {TokenApiView.class, TokenAPI.class})
    public static class TokenAPIConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return TokenAPI.URL;
        }
    }

    @Order(187)
    @Configuration
    @ConditionalOnProperty(havingValue = "true", name = "openid.connect.endpoints.api.scope.enabled", matchIfMissing = true)
    @Import(value = {ScopeAPI.class})
    public static class ScopeAPIConfiguration extends ApiResourceServerConfig{
        @Override
        protected String getPattern(){
            return ScopeAPI.URL;
        }
    }
}