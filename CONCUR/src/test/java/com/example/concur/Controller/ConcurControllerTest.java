package com.example.concur.Controller;

import com.example.concur.Entity.UserDetails;
import com.example.concur.Service.KafkaProducerService;
import com.example.concur.Service.TokenService;
import com.example.concur.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConcurController.class)
public class ConcurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @MockBean
    private UserDetailsRepository userDetailsRepository;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUserDetails_validToken() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("test@example.com");
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");

        Mockito.when(tokenService.isValidToken(anyString())).thenReturn(true);
        Mockito.when(tokenService.decodeEmailFromToken(anyString())).thenReturn("test@example.com");
        Mockito.when(tokenService.isTokenExpired(anyString())).thenReturn(false);
        Mockito.when(userDetailsRepository.findByEmail(anyString())).thenReturn(Optional.of(userDetails));

        mockMvc.perform(get("/concur/user-details/validToken"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDetails)));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUserDetails_invalidToken() throws Exception {
        Mockito.when(tokenService.isValidToken(anyString())).thenReturn(false);

        mockMvc.perform(get("/concur/user-details/invalidToken"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUserDetails_tokenExpired() throws Exception {
        Mockito.when(tokenService.isValidToken(anyString())).thenReturn(true);
        Mockito.when(tokenService.decodeEmailFromToken(anyString())).thenReturn("test@example.com");
        Mockito.when(tokenService.isTokenExpired(anyString())).thenReturn(true);

        mockMvc.perform(get("/concur/user-details/expiredToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token expired. Please refresh your token."));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUserDetails_userNotFound() throws Exception {
        Mockito.when(tokenService.isValidToken(anyString())).thenReturn(true);
        Mockito.when(tokenService.decodeEmailFromToken(anyString())).thenReturn("test@example.com");
        Mockito.when(tokenService.isTokenExpired(anyString())).thenReturn(false);
        Mockito.when(userDetailsRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/concur/user-details/validToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User details not found"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetUserDetails_tokenIsRequired() throws Exception {
        mockMvc.perform(get("/concur/user-details/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin_concur", roles = {"ADMIN"})
    public void testRefreshToken_validToken() throws Exception {
        Mockito.when(tokenService.isValidToken(anyString())).thenReturn(true);
        Mockito.when(tokenService.decodeEmailFromToken(anyString())).thenReturn("test@example.com");
        Mockito.when(tokenService.fetchNewToken(anyString())).thenReturn("newValidToken");
        Mockito.when(tokenService.isTokenExpired(anyString())).thenReturn(false);

        mockMvc.perform(get("/concur/refresh-token/validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("newValidToken"));
    }

    @Test
    @WithMockUser(username = "admin_concur", roles = {"ADMIN"})
    public void testRefreshToken_invalidToken() throws Exception {
        Mockito.when(tokenService.isValidToken(anyString())).thenReturn(false);

        mockMvc.perform(get("/concur/refresh-token/invalidToken"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    @WithMockUser(username = "admin_concur", roles = {"ADMIN"})
    public void testRefreshToken_tokenIsRequired() throws Exception {
        mockMvc.perform(get("/concur/refresh-token/"))
                .andExpect(status().isNotFound());
    }
}
