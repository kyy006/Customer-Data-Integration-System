package com.example.cwt.Controller;

import com.example.cwt.Entity.Token;
import com.example.cwt.Service.TokenService;
import com.example.cwt.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TokenController.class)
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    @WithMockUser(username = "admin_cwt", roles = {"ADMIN"})
    public void testGetToken_existingToken() throws Exception {
        Token token = new Token("test@example.com", "token123", LocalDateTime.now().plusMinutes(5));
        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(token));

        mockMvc.perform(get("/token/get/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    @WithMockUser(username = "admin_cwt", roles = {"ADMIN"})
    public void testGetToken_newToken() throws Exception {
        Token token = new Token("test@example.com", "newToken123", LocalDateTime.now().plusHours(1));
        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(tokenService.refreshToken(anyString())).thenReturn(token);

        mockMvc.perform(get("/token/get/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("newToken123"));
    }

//    @Test
//    @WithMockUser(username = "admin_cwt", roles = {"ADMIN"})
//    public void testDeleteToken_existingToken() throws Exception {
//        Token token = new Token("test@example.com", "token123", LocalDateTime.now().plusMinutes(5));
//        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.of(token));
//
//        mockMvc.perform(delete("/token/delete/test@example.com"))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(tokenRepository).delete(token);
//    }

//    @Test
//    @WithMockUser(username = "admin_cwt", roles = {"ADMIN"})
//    public void testDeleteToken_nonExistingToken() throws Exception {
//        when(tokenRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
//
//        mockMvc.perform(delete("/token/delete/test@example.com"))
//                .andExpect(status().isNotFound());
//    }
}
