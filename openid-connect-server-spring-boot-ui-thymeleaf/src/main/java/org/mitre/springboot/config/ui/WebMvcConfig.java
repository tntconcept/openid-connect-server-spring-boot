package org.mitre.springboot.config.ui;

import org.mitre.openid.connect.config.JsonMessageSource;
import org.mitre.openid.connect.web.ServerConfigInterceptor;
import org.mitre.openid.connect.web.UserInfoInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public UserInfoInterceptor getUserInfoInterceptor() {
		return new UserInfoInterceptor();
	}
	
	@Bean
	public ServerConfigInterceptor getServerConfigInterceptor() {
		return new ServerConfigInterceptor();
	}
	
	@Bean
	@ConfigurationProperties(prefix="openid.connect.jsonMessageSource")
	public JsonMessageSource messageSource() {
		return new JsonMessageSource();
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserInfoInterceptor());
        registry.addInterceptor(getServerConfigInterceptor());
    }
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/resources/");
    }
	

}
