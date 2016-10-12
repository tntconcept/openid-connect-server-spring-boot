package org.mitre.springboot.config;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.mitre.jose.keystore.JWKSetKeyStore;
import org.mitre.jwt.encryption.service.JWTEncryptionAndDecryptionService;
import org.mitre.jwt.encryption.service.impl.DefaultJWTEncryptionAndDecryptionService;
import org.mitre.jwt.signer.service.JWTSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.DefaultJWTSigningAndValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;

@Configuration
public class CryptoConfig {
	
	@Bean
	@ConditionalOnMissingBean(JWKSetKeyStore.class)
	public JWKSetKeyStore defaultKeyStore(@Value("${openid.connect.crypto.keystore.path}") Resource location) {
		JWKSetKeyStore keystore = new JWKSetKeyStore();
		keystore.setLocation(location);
		return keystore;
	}
	
	@Bean
	@ConditionalOnMissingBean(JWTSigningAndValidationService.class)
	public JWTSigningAndValidationService defaultJwtSigningAndValidationService(
			JWKSetKeyStore keystore,
			@Value("${openid.connect.crypto.signing.defaultSignerKeyId}") String defaultSignerKeyId,
			@Value("${openid.connect.crypto.signing.defaultSigningAlgorithmName}") String defaultSigningAlgorithmName) throws NoSuchAlgorithmException, InvalidKeySpecException {
		DefaultJWTSigningAndValidationService s = new DefaultJWTSigningAndValidationService(keystore);
		s.setDefaultSignerKeyId(defaultSignerKeyId);
		s.setDefaultSigningAlgorithmName(defaultSigningAlgorithmName);
		return s;
	}
	
	@Bean
	@ConditionalOnMissingBean(JWTEncryptionAndDecryptionService.class)
	public JWTEncryptionAndDecryptionService defaultJwtEncryptionAndDecryptionService(
			JWKSetKeyStore keystore, 
			@Value("${openid.connect.crypto.encrypt.defaultAlgorithm}") JWEAlgorithm defaultAlgorithm,
			@Value("${openid.connect.crypto.encrypt.defaultDecryptionKeyId}") String defaultDecryptionKeyId,
			@Value("${openid.connect.crypto.encrypt.defaultEncryptionKeyId}") String defaultEncryptionKeyId
			) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
		DefaultJWTEncryptionAndDecryptionService s = new DefaultJWTEncryptionAndDecryptionService(keystore);
		s.setDefaultAlgorithm(defaultAlgorithm);
		s.setDefaultDecryptionKeyId(defaultDecryptionKeyId);
		s.setDefaultEncryptionKeyId(defaultEncryptionKeyId);
		return s;
	}
}
