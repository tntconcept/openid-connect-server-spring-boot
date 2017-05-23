package org.mitre.springboot.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"org.mitre.oauth2.model", "org.mitre.openid.connect.model", "org.mitre.uma.model"})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class EclipseLinkJpaConfig extends JpaBaseConfiguration{

    public EclipseLinkJpaConfig(final DataSource dataSource, final JpaProperties properties,
            final ObjectProvider<JtaTransactionManager> jtaTransactionManager,
            final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers){
        super(dataSource, properties, jtaTransactionManager, transactionManagerCustomizers);
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter(){
        return new EclipseLinkJpaVendorAdapter();
    }

    /* 
     * Explicitly defining this bean as MITREid code looks for a TransactionManager named "defaultTransactionManager"
     * */
    @Bean(name = "defaultTransactionManager")
    @Override
    public PlatformTransactionManager transactionManager(){
        return new JpaTransactionManager();
    }

    /*
     * Explicitly defining this bean as MITRE code looks for a persistenceUnit named "defaultPersistenceUnit"
     * 
     * */
    @Bean
    @Primary
    @Override
    @ConditionalOnMissingBean({LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class})
    @ConfigurationProperties(prefix = "openid.connect.jpa")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final EntityManagerFactoryBuilder factoryBuilder){
        final LocalContainerEntityManagerFactoryBean factory = super.entityManagerFactory(factoryBuilder);
        factory.getJpaPropertyMap().put("eclipselink.weaving", "false");
        factory.getJpaPropertyMap().put("eclipselink.cache.shared.default", "false");
        factory.setPersistenceUnitName("defaultPersistenceUnit");
        return factory;
    }

    @Override
    protected Map<String, Object> getVendorProperties(){
        final Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
        return vendorProperties;
    }

}
