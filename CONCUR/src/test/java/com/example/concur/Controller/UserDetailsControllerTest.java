package com.example.concur.Controller;

import com.example.concur.Entity.UserDetails;
import com.example.concur.config.NoSecurityConfig;
import com.example.concur.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserDetailsController.class)
@Import(NoSecurityConfig.class)
@ActiveProfiles("test")
public class UserDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsRepository userDetailsRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllUserDetails() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("test@example.com");
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");

        Mockito.when(userDetailsRepository.findAll()).thenReturn(Collections.singletonList(userDetails));

        mockMvc.perform(get("/user-details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(userDetails))));
    }

    @Test
    public void testGetUserDetailsById() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1L);
        userDetails.setEmail("test@example.com");
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");

        Mockito.when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(userDetails));

        mockMvc.perform(get("/user-details/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDetails)));
    }

    @Test
    public void testGetUserDetailsById_NotFound() throws Exception {
        Mockito.when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/user-details/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUserDetails() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("test@example.com");
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");

        Mockito.when(userDetailsRepository.save(any(UserDetails.class))).thenReturn(userDetails);

        mockMvc.perform(post("/user-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDetails)));
    }

    @Test
    public void testCreateUserDetails_Conflict() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("test@example.com");

        Mockito.when(userDetailsRepository.findByEmail(anyString())).thenReturn(Optional.of(userDetails));

        mockMvc.perform(post("/user-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateUserDetails() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1L);
        userDetails.setEmail("test@example.com");
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");

        Mockito.when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(userDetails));
        Mockito.when(userDetailsRepository.save(any(UserDetails.class))).thenReturn(userDetails);

        mockMvc.perform(put("/user-details/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDetails)));
    }

    @Test
    public void testUpdateUserDetails_NotFound() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("test@example.com");

        Mockito.when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/user-details/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserDetailsById() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1L);

        Mockito.when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.of(userDetails));

        mockMvc.perform(delete("/user-details/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserDetailsById_NotFound() throws Exception {
        Mockito.when(userDetailsRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/user-details/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserDetailsByEmail() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("test@example.com");

        Mockito.when(userDetailsRepository.findByEmail(anyString())).thenReturn(Optional.of(userDetails));

        mockMvc.perform(delete("/user-details/delete/test@example.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserDetailsByEmail_NotFound() throws Exception {
        Mockito.when(userDetailsRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/user-details/delete/test@example.com"))
                .andExpect(status().isNotFound());
    }
}