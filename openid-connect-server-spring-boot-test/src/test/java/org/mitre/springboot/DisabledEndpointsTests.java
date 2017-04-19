package org.mitre.springboot;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.springboot.DisabledEndpointsTests.DisabledBootApplication;
import org.mitre.springboot.config.annotation.EnableOpenIDConnectServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test that application endpoints are disabled properly via application properties
 * 
 * @author barretttucker
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DisabledBootApplication.class}, webEnvironment = WebEnvironment.DEFINED_PORT, properties = {
        "server.port=8091", "batch.metrics.enabled=true"})
@ActiveProfiles("endpoints-disabled")
public class DisabledEndpointsTests{

    @Configuration
    @SpringBootApplication
    @EnableOpenIDConnectServer
    public static class DisabledBootApplication extends SpringBootServletInitializer{
        public static void main(final String[] args){
            SpringApplication.run(DisabledBootApplication.class, args);
        }

    }

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected FilterChainProxy springSecurityFilterChain;

    protected MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void adminGetApiApprovedNotFound() throws Exception{
        mockMvc.perform(get("/api/approved")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiBlacklistNotFound() throws Exception{
        mockMvc.perform(get("/api/blacklist")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiClientsNotFound() throws Exception{
        mockMvc.perform(get("/api/clients")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiDataNotFound() throws Exception{
        mockMvc.perform(get("/api/data")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiTokenNotFound() throws Exception{
        mockMvc.perform(get("/api/tokens/access")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiScopeNotFound() throws Exception{
        mockMvc.perform(get("/api/scopes")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiStatsNotFound() throws Exception{
        mockMvc.perform(get("/api/stats/summary")).andExpect(status().is(404));
    }

    @Test
    public void adminGetApiWhitelistNotFound() throws Exception{
        mockMvc.perform(get("/api/whitelist")).andExpect(status().is(404));
    }

    @Test
    public void adminGetDynamicClientRegistrationEndpointNotFound() throws Exception{
        mockMvc.perform(post("/register").with(csrf())).andExpect(status().is(404));
    }

    @Test
    public void adminGetJwkEndpointNotFound() throws Exception{
        mockMvc.perform(get("/jwk")).andExpect(status().is(404));
    }

    @Test
    public void adminGetUserInfoEndpointNotFound() throws Exception{
        mockMvc.perform(get("/userinfo")).andExpect(status().is(404));
    }

    @Test
    public void adminGetDiscoveryEndpointNotFound() throws Exception{
        mockMvc.perform(get("/.well-known/openid-configuration")).andExpect(status().is(404));
    }

    /* FIXME disabling all API endpoints is giving a configuration error
    @Test
    public void adminGetProtectedResourceRegistrationEndpointNotFound() throws Exception {
    	mockMvc.perform(post("/resource")).andExpect(status().is(404));
    }
    */
}
