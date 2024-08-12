package com.example.customersystem.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    public void testSendUserEmail() {
        String email = "test@example.com";

        kafkaProducerService.sendUserEmail(email);

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send("user_email_topic", email);
    }
}
