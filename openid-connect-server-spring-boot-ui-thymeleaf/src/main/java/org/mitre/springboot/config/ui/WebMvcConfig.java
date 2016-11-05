package org.mitre.springboot.config.ui;

import org.mitre.openid.connect.config.JsonMessageSource;
import org.mitre.openid.connect.web.RootController;
import org.mitre.openid.connect.web.ServerConfigInterceptor;
import org.mitre.openid.connect.web.UserInfoInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	@ConditionalOnMissingBean(UserInfoInterceptor.class)
	public UserInfoInterceptor getUserInfoInterceptor() {
		return new UserInfoInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(ServerConfigInterceptor.class)
	public ServerConfigInterceptor getServerConfigInterceptor() {
		return new ServerConfigInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(JsonMessageSource.class)
	@ConfigurationProperties(prefix = "openid.connect.jsonMessageSource")
	public JsonMessageSource messageSource(
			@Value("classpath:/static/resources/js/locale/") Resource baseDirectory,
			@Value("true") Boolean useCodeAsDefaultMessage) {
		JsonMessageSource jsonMessageSource = new JsonMessageSource();
		jsonMessageSource.setBaseDirectory(baseDirectory);
		jsonMessageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
		return jsonMessageSource;
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
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"classpath:/static/resources/");
	}
	
	@Configuration
	@Import(RootController.class)
	public static class RootControllerConfiguration {}

	

}
