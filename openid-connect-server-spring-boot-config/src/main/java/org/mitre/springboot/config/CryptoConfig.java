package org.mitre.springboot.config;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.mitre.jose.keystore.JWKSetKeyStore;
import org.mitre.jwt.encryption.service.JWTEncryptionAndDecryptionService;
import org.mitre.jwt.encryption.service.impl.DefaultJWTEncryptionAndDecryptionService;
import org.mitre.jwt.signer.service.JWTSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.DefaultJWTSigningAndValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;

@Configuration
public class CryptoConfig{

    @Bean
    @ConditionalOnMissingBean(JWKSetKeyStore.class)
    public JWKSetKeyStore defaultKeyStore(@Value("${openid.connect.crypto.keystore.path}")
    final Resource location){
        final JWKSetKeyStore keystore = new JWKSetKeyStore();
        keystore.setLocation(location);
        return keystore;
    }

    @Bean
    @ConditionalOnMissingBean(JWTSigningAndValidationService.class)
    public JWTSigningAndValidationService defaultJwtSigningAndValidationService(final JWKSetKeyStore keystore,
            @Value("${openid.connect.crypto.signing.defaultSignerKeyId}")
            final String defaultSignerKeyId, @Value("${openid.connect.crypto.signing.defaultSigningAlgorithmName}")
            final String defaultSigningAlgorithmName) throws NoSuchAlgorithmException, InvalidKeySpecException{
        final DefaultJWTSigningAndValidationService s = new DefaultJWTSigningAndValidationService(keystore);
        s.setDefaultSignerKeyId(defaultSignerKeyId);
        s.setDefaultSigningAlgorithmName(defaultSigningAlgorithmName);
        return s;
    }

    @Bean
    @ConditionalOnMissingBean(JWTEncryptionAndDecryptionService.class)
    public JWTEncryptionAndDecryptionService defaultJwtEncryptionAndDecryptionService(final JWKSetKeyStore keystore,
            @Value("${openid.connect.crypto.encrypt.defaultAlgorithm}")
            final JWEAlgorithm defaultAlgorithm, @Value("${openid.connect.crypto.encrypt.defaultDecryptionKeyId}")
            final String defaultDecryptionKeyId, @Value("${openid.connect.crypto.encrypt.defaultEncryptionKeyId}")
            final String defaultEncryptionKeyId) throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException{
        final DefaultJWTEncryptionAndDecryptionService s = new DefaultJWTEncryptionAndDecryptionService(keystore);
        s.setDefaultAlgorithm(defaultAlgorithm);
        s.setDefaultDecryptionKeyId(defaultDecryptionKeyId);
        s.setDefaultEncryptionKeyId(defaultEncryptionKeyId);
        return s;
    }

    @Bean("clientPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.clients.bcrypt", name = "enabled")
    public PasswordEncoder clientBCryptPasswordEncoder(@Qualifier("clientPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final BCryptPasswordEncoderConfig bcryptConfig = passwordEncoderConfig.getBcrypt();

        return new BCryptPasswordEncoder(bcryptConfig.getStrength());
    }

    @Bean("userPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.users.bcrypt", name = "enabled")
    public PasswordEncoder userBCryptPasswordEncoder(@Qualifier("userPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final BCryptPasswordEncoderConfig bcryptConfig = passwordEncoderConfig.getBcrypt();

        return new BCryptPasswordEncoder(bcryptConfig.getStrength());
    }

    @Bean("clientPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.clients.scrypt", name = "enabled")
    public PasswordEncoder clientSCryptPasswordEncoder(@Qualifier("clientPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final SCryptPasswordEncoderConfig scryptConfig = passwordEncoderConfig.getSCrypt();

        return new SCryptPasswordEncoder(scryptConfig.getCpuCost(), scryptConfig.getMemoryCost(),
                scryptConfig.getParallelization(), scryptConfig.getKeyLength(), scryptConfig.getSaltLength());
    }

    @Bean("userPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.users.scrypt", name = "enabled")
    public PasswordEncoder userSCryptPasswordEncoder(@Qualifier("userPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final SCryptPasswordEncoderConfig scryptConfig = passwordEncoderConfig.getSCrypt();

        return new SCryptPasswordEncoder(scryptConfig.getCpuCost(), scryptConfig.getMemoryCost(),
                scryptConfig.getParallelization(), scryptConfig.getKeyLength(), scryptConfig.getSaltLength());
    }

    @Bean("clientPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.clients.standard", name = "enabled")
    public PasswordEncoder clientStandardPasswordEncoder(@Qualifier("clientPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final StandardPasswordEncoderConfig standardConfig = passwordEncoderConfig.getStandard();

        return new StandardPasswordEncoder(standardConfig.getSecret());
    }

    @Bean("userPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.users.standard", name = "enabled")
    public PasswordEncoder userStandardPasswordEncoder(@Qualifier("userPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final StandardPasswordEncoderConfig standardConfig = passwordEncoderConfig.getStandard();

        return new StandardPasswordEncoder(standardConfig.getSecret());
    }

    @Bean("clientPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.clients.pbkdf2", name = "enabled")
    public PasswordEncoder clientPbkdf2PasswordEncoder(@Qualifier("clientPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final Pbkdf2PasswordEncoderConfig pbkdf2Config = passwordEncoderConfig.getPbkdf2();

        return new Pbkdf2PasswordEncoder(pbkdf2Config.getSecret());
    }

    @Bean("userPasswordEncoder")
    @ConditionalOnProperty(prefix = "openid.connect.crypto.password-encoder.users.pbkdf2", name = "enabled")
    public PasswordEncoder userPbkdf2PasswordEncoder(@Qualifier("userPasswordEncoders")
    final PasswordEncoderTypeConfig passwordEncoderConfig){
        final Pbkdf2PasswordEncoderConfig pbkdf2Config = passwordEncoderConfig.getPbkdf2();

        return new Pbkdf2PasswordEncoder(pbkdf2Config.getSecret());
    }

    @Bean
    @ConditionalOnMissingBean(name = "userPasswordEncoder")
    public PasswordEncoder userPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(name = "clientPasswordEncoder")
    public PasswordEncoder clientPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @ConfigurationProperties(prefix = "openid.connect.crypto.password-encoder.users")
    public PasswordEncoderTypeConfig userPasswordEncoders(){
        return new PasswordEncoderTypeConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "openid.connect.crypto.password-encoder.clients")
    public PasswordEncoderTypeConfig clientPasswordEncoders(){
        return new PasswordEncoderTypeConfig();
    }

    public static class PasswordEncoderTypeConfig{
        private final BCryptPasswordEncoderConfig bcrypt = new BCryptPasswordEncoderConfig();
        private final SCryptPasswordEncoderConfig scrypt = new SCryptPasswordEncoderConfig();
        private final StandardPasswordEncoderConfig standard = new StandardPasswordEncoderConfig();
        private final Pbkdf2PasswordEncoderConfig pbkdf2 = new Pbkdf2PasswordEncoderConfig();

        public BCryptPasswordEncoderConfig getBcrypt(){
            return bcrypt;
        }

        public SCryptPasswordEncoderConfig getSCrypt(){
            return scrypt;
        }

        public StandardPasswordEncoderConfig getStandard(){
            return standard;
        }

        public Pbkdf2PasswordEncoderConfig getPbkdf2(){
            return pbkdf2;
        }

    }

    public static class BCryptPasswordEncoderConfig{
        private int strength = -1;

        public int getStrength(){
            return strength;
        }

        public void setStrength(final int strength){
            this.strength = strength;
        }

    }

    public static class SCryptPasswordEncoderConfig{

        private int cpuCost = 16384;
        private int memoryCost = 8;
        private int parallelization = 1;
        private int keyLength = 32;
        private int saltLength = 64;

        public int getCpuCost(){
            return cpuCost;
        }

        public void setCpuCost(final int cpuCost){
            this.cpuCost = cpuCost;
        }

        public int getMemoryCost(){
            return memoryCost;
        }

        public void setMemoryCost(final int memoryCost){
            this.memoryCost = memoryCost;
        }

        public int getParallelization(){
            return parallelization;
        }

        public void setParallelization(final int parallelization){
            this.parallelization = parallelization;
        }

        public int getKeyLength(){
            return keyLength;
        }

        public void setKeyLength(final int keyLength){
            this.keyLength = keyLength;
        }

        public int getSaltLength(){
            return saltLength;
        }

        public void setSaltLength(final int saltLength){
            this.saltLength = saltLength;
        }

    }

    public static class StandardPasswordEncoderConfig{
        private String secret = "";

        public String getSecret(){
            return secret;
        }

        public void setSecret(final String secret){
            this.secret = secret;
        }
    }

    public static class Pbkdf2PasswordEncoderConfig{
        private String secret = "";

        public String getSecret(){
            return secret;
        }

        public void setSecret(final String secret){
            this.secret = secret;
        }
    }

}
