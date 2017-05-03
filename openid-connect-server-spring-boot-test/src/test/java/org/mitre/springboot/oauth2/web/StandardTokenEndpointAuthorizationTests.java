package org.mitre.springboot.oauth2.web;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@Transactional
@ActiveProfiles(profiles = "standard")
public class StandardTokenEndpointAuthorizationTests extends OAuthAuthorizationTestsBase{

    @Test
    public void givenAClientCredentialsAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt()
            throws Exception{

        getClientAccessToken("client_standard", "secret");
    }

    @Test
    public void givenAPasswordAuthenticationShouldBeAuthenticatedWhenPasswordIsEncodedWithBCrypt() throws Exception{
        getToken("admin_standard", "password", "token", "access_token");
    }
}
