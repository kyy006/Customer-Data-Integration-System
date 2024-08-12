package com.example.concur.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendUserDetails() {
        String details = "userDetails";

        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.complete(new SendResult<>(null, null));

        when(kafkaTemplate.send(eq("user_details_topic"), eq(details))).thenReturn(future);

        kafkaProducerService.sendUserDetails(details);

        verify(kafkaTemplate, times(1)).send("user_details_topic", details);
    }
}