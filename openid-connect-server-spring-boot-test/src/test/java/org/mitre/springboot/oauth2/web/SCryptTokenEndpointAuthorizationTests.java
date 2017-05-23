package org.mitre.springboot.oauth2.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@Transactional
@ActiveProfiles(profiles = "scrypt")
public class SCryptTokenEndpointAuthorizationTests extends OAuthAuthorizationTestsBase{

    @Test
    public void givenAClientCredentialsAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt()
            throws Exception{

        getClientAccessToken("client_scrypt", "secret");
    }

    @Test
    public void givenAPasswordAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt() throws Exception{

        getToken("admin_scrypt", "password", "token", "access_token");
    }
}
