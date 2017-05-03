package org.mitre.springboot.oauth2.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@Transactional
@ActiveProfiles(profiles = "bcrypt")
public class BCryptTokenEndpointAuthorizationTests extends OAuthAuthorizationTestsBase{

    @Test
    public void givenAClientCredentialsAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt()
            throws Exception{

        getClientAccessToken("client_bcrypt", "secret");
    }

    @Test
    public void givenAPasswordAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt() throws Exception{

        getToken("admin_bcrypt", "password", "token", "access_token");
    }
}
