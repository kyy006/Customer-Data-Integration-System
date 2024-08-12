package com.example.cwt.Service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.mockito.Mockito.*;

@EmbeddedKafka
public class KafkaConsumerServiceTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    public KafkaConsumerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsume() {
        String email = "test@example.com";

        kafkaConsumerService.consume(email);

        verify(tokenService, times(1)).generateToken(email);
    }
}