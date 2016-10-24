package org.mitre.springboot.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"org.mitre.oauth2.model","org.mitre.openid.connect.model"} )
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class EclipseLinkJpaConfig extends JpaBaseConfiguration {

	@Autowired
	private JpaProperties properties;

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new EclipseLinkJpaVendorAdapter();
	}

	/* 
	 * Explicitly defining this bean as MITREid code looks for a TransactionManager named "defaultTransactionManager"
	 * */
	@Bean(name="defaultTransactionManager")
	@Override
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager();
	}
	
	/*
	 * Explicitly defining this bean as MITRE code looks for a persistenceUnit named "defaultPersistenceUnit"
	 * */
	@Bean
	@Primary
	@Override
	@ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class })
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder) {
		LocalContainerEntityManagerFactoryBean factory = super.entityManagerFactory(factoryBuilder);
		factory.setPersistenceUnitName("defaultPersistenceUnit");
		return factory;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
		return vendorProperties;
	}

}
