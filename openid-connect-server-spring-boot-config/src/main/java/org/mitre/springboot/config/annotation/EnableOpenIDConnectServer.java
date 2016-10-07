package org.mitre.springboot.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mitre.springboot.config.OpenIDConnectServerConfig;
import org.springframework.context.annotation.Import;

/**
 * Enables OpenID Connect Server configuration facility.
 * To be used together with {@link org.springframework.context.annotation.Configuration Configuration}
 * or {@link org.springframework.boot.autoconfigure.SpringBootApplication SpringBootApplication} classes.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OpenIDConnectServerConfig.class)
public @interface EnableOpenIDConnectServer {


}

