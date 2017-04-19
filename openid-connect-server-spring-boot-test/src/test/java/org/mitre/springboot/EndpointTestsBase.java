package org.mitre.springboot;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestBootApplication.class})
@WebAppConfiguration
public abstract class EndpointTestsBase{

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected FilterChainProxy springSecurityFilterChain;

    protected MockMvc mockMvc;
    protected MockHttpSession mockSession;
    protected ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).addFilter(springSecurityFilterChain).build();
        mockSession = new MockHttpSession(context.getServletContext(), UUID.randomUUID().toString());
    }

    protected void adminSession() throws Exception{
        loginSession("admin", "password");
    }

    protected void userSession() throws Exception{
        loginSession("user", "password");
    }

    protected void loginSession(final String username, final String password) throws Exception{
        mockMvc.perform(post("/login").session(mockSession).with(csrf()).param("username", username).param("password",
                password)).andExpect(status().isFound()).andExpect(redirectedUrl("/")).andReturn().getRequest();
    }
}
