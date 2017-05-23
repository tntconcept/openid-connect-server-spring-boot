package org.mitre.springboot.oauth2.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@Transactional
@ActiveProfiles(profiles = "pbkdf2")
public class Pbkdf2TokenEndpointAuthorizationTests extends OAuthAuthorizationTestsBase{

    @Test
    public void givenAClientCredentialsAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt()
            throws Exception{

        final Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder("salt");

        final String secret = encoder.encode("secret");
        final String password = encoder.encode("password");

        getClientAccessToken("client_pbkdf2", "secret");
    }

    @Test
    public void givenAPasswordAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt() throws Exception{

        final Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder("salt");
        final String password = encoder.encode("password");
        getToken("admin_pbkdf2", "password", "token", "access_token");
    }
}
