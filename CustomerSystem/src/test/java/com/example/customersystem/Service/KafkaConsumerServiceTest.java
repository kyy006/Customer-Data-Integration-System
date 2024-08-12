package com.example.customersystem.Service;

import com.example.customersystem.Entity.UserDetails;
import com.example.customersystem.Entity.UserProfile;
import com.example.customersystem.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;



    @Test
    public void testConsume_UserProfileDoesNotExist() throws Exception {
        String message = "{\"email\":\"test@example.com\"}";

        Mockito.when(userProfileRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        kafkaConsumerService.consume(message);

        // You can also verify logging or other behaviors here
    }


}
