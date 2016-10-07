package org.mitre.boot.security.oidc;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mitre.springboot.EndpointTestsBase;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nimbusds.jwt.SignedJWT;


public class JwtAuthorizeEndpointFlowTests extends EndpointTestsBase {

	//Test data based upon in memory default db scripts
	String username = "user";
	String password = "password";
	String nonce = UUID.randomUUID().toString();
	String state = UUID.randomUUID().toString();
	String userSubject = "01921.FLANRJQW";
    String clientId = "client";
	String secret = "secret";
	
	/**
	 * Walks through the flow around a OIDC JWT Redirect authorize request
	 * @throws Exception
	 */
	@Test
	public void codeTokenIdTokenResponseTypeAuthorizeTest() throws Exception {

		//Request authorize, expect login redirect
        mockMvc.perform(
			get("/authorize")
			.session(mockSession)
			.param("response_type", "code token id_token")
			.param("client_id", clientId)
			.param("redirect_uri", "http://localhost/")
			.param("scope", "openid profile email")
			.param("state", state)
			.param("nonce", nonce)
			.param("aud", "client_id")
			)
			.andDo(print())
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("http://localhost/login"))
			;     

        //Login to establish session
        String authorizeLocationHeader = mockMvc.perform(
			post("/login")
			.session(mockSession)
			.with(csrf())
			.param("username", username)
			.param("password", password))
			.andDo(print())
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("http://localhost/authorize*")
			) 
			.andReturn()
			.getResponse()
			.getHeader("Location")
			;
        
        //Follow redirect back to authorize view
        mockMvc.perform(
			get(authorizeLocationHeader)
			.session(mockSession))
			.andDo(print())
			.andExpect(status().isOk())
			;
        
        MvcResult result = mockMvc.perform(
			post("/authorize")
			.session(mockSession)
			.with(csrf())
			.param("scope_openid", "openid")
			.param("scope_profile", "profile")
			.param("remember", "none")
			.param("user_oauth_approval", "true")
			.param("authorize", "Authorize")
			)
			.andDo(print())	
			.andExpect(redirectedUrlPattern("http://localhost/*"))
			.andReturn()
			;
        
        //In this flow the tokens are URL fragments on the redirect URI
        String redirectUrl = result.getResponse().getRedirectedUrl().replace("#","?");
        List<NameValuePair> queryParams = URLEncodedUtils.parse(new URI(redirectUrl), "UTF-8");
        HashMap<String, Object>  queryParamsByName  = new HashMap<String,Object>();
        for(NameValuePair pair: queryParams) {
        	queryParamsByName.put(pair.getName(), pair.getValue());
        }
        
        Assert.assertEquals(state, queryParamsByName.get("state"));
        Assert.assertEquals("Bearer", queryParamsByName.get("token_type"));
        Assert.assertEquals("3599", queryParamsByName.get("expires_in"));
        Assert.assertEquals("openid profile", queryParamsByName.get("scope"));
       
        SignedJWT accessToken = SignedJWT.parse(queryParamsByName.get("access_token").toString());
        validateAccessToken(accessToken);

        SignedJWT idToken = SignedJWT.parse(queryParamsByName.get("id_token").toString());
        validateIdToken(idToken);
              
	}
	/**
	 * Walks through the UI flow around a Authorization Code redirect /authorize request
	 * @throws Exception
	 */
	@Test
	public void codeResponseTypeLoginFlowTest() throws Exception {

		
		//Request authorize, expect login redirect
        mockMvc.perform(
			get("/authorize")
			.session(mockSession)
			.param("response_type", "code")
			.param("client_id", clientId)
			.param("redirect_uri", "http://localhost/")
			.param("scope", "openid profile email")
			.param("state", state)
			.param("nonce", nonce)
			)
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("http://localhost/login"))
			.andDo(print())
			;     
        
        //Login to establish session
        String authorizeLocation = mockMvc.perform(
			post("/login")
			.session(mockSession)
			.with(csrf())
			.param("username", username)
			.param("password", password))
			.andDo(print())
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("http://localhost/authorize*")) //?response_type=code%20token%20id_token&client_id=client&redirect_uri=http://localhost/&scope=openid%20profile%20email&state=af0ifjsldkj&nonce=n-0S6_WzA2Mj
			.andReturn()
			.getResponse()
			.getHeader("Location")
			;
        
        //Follow redirect back to authorize view
        mockMvc.perform(
			get(authorizeLocation)
			.session(mockSession))
			//.with(csrf())
			.andDo(print())
			.andExpect(status().isOk())
			;
        
        //Authorization request is in session, now user approves the scopes and grants
        MvcResult result = mockMvc.perform(
			post("/authorize")
			.session(mockSession)
			.with(csrf())
			.param("scope_openid", "openid")
			.param("scope_profile", "profile")
			.param("remember", "none") //none, until-revoked
			.param("user_oauth_approval", "true")
			.param("authorize", "Authorize")
			)
	        .andDo(print())	
	        .andExpect(redirectedUrlPattern("http://localhost/*"))
	        .andReturn()
	        ;
        
        //Extract authorization code for Oauth2 /token authorization_code request
        
        List<NameValuePair> params = URLEncodedUtils.parse(new URI(result.getResponse().getRedirectedUrl()), "UTF-8");
        String code = null;
        for(NameValuePair pair: params) {
        	if(pair.getName().equals("code")) {
	        	code =  pair.getValue();
        	}
        }
        Assert.assertNotNull(code);
        
        //Token endpoint request
		result = mockMvc.perform(
			post("/token")
			.with(httpBasic(clientId,secret))
			.param("grant_type", "authorization_code")
			.param("code", code)
			.param("redirect_uri", "http://localhost/")
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("token_type", is("Bearer")))
			.andExpect(jsonPath("scope", is("openid profile")))
			.andExpect(jsonPath("expires_in", is(3599)))
			.andReturn()      
	         ;     
		String json = result.getResponse().getContentAsString();  
        HashMap<String,Object> map = mapper.readValue(json, new TypeReference<HashMap<String,Object>>(){}); 
        SignedJWT accessToken = SignedJWT.parse(map.get("access_token").toString());
        validateAccessToken(accessToken);

        SignedJWT idToken = SignedJWT.parse(map.get("id_token").toString());
        validateIdToken(idToken);
	}
	
	
	protected void validateAccessToken(SignedJWT accessToken) throws ParseException {
        Assert.assertEquals("client",accessToken.getJWTClaimsSet().getAudience().get(0));
        Assert.assertEquals("http://localhost:-1/",accessToken.getJWTClaimsSet().getIssuer());
        Assert.assertNotNull(accessToken.getJWTClaimsSet().getExpirationTime());
        Assert.assertNotNull(accessToken.getJWTClaimsSet().getIssueTime());
        Assert.assertNotNull(accessToken.getJWTClaimsSet().getJWTID());
        
        //TODO validate RSA signature
	}
	
	protected void validateIdToken(SignedJWT idToken) throws ParseException {

        Assert.assertEquals(userSubject, idToken.getJWTClaimsSet().getSubject());
        Assert.assertEquals(nonce, idToken.getJWTClaimsSet().getStringClaim("nonce"));
        Assert.assertEquals("client",idToken.getJWTClaimsSet().getAudience().get(0));
        Assert.assertEquals("rsa1",idToken.getJWTClaimsSet().getStringClaim("kid"));
        Assert.assertEquals("http://localhost:-1/",idToken.getJWTClaimsSet().getIssuer());
        
        Assert.assertNotNull(idToken.getJWTClaimsSet().getExpirationTime());
        Assert.assertNotNull(idToken.getJWTClaimsSet().getIssueTime());
        Assert.assertNotNull(idToken.getJWTClaimsSet().getJWTID());

        //TODO validate at_hash
        //Assert.assertEquals("?",idToken.getJWTClaimsSet().getStringClaim("at_hash"));
        
        //TODO validate RSA signature
        
     }
	
}	
