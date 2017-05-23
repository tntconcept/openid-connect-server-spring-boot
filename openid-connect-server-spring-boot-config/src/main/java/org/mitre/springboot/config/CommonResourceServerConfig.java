package org.mitre.springboot.config;

import org.mitre.oauth2.service.OAuth2TokenEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@Order(170)
public class CommonResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Autowired
    private final OAuth2TokenEntityService oAuth2TokenEntityService;

    public CommonResourceServerConfig(final OAuth2TokenEntityService oAuth2TokenEntityService,
            final ResourceServerProperties resourceServerConfiguration){
        this.oAuth2TokenEntityService = oAuth2TokenEntityService;

        //Change order of filter ResourceServer
        resourceServerConfiguration.setFilterOrder(170);
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) throws Exception{
        resources.stateless(false);
        resources.tokenServices(oAuth2TokenEntityService);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception{
    }

}
