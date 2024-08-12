package com.example.cwt.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);


    @Autowired
    private TokenService tokenService;


    @KafkaListener(topics = "user_email_topic", groupId = "group_id")
    public void consume(String email) {
        // Generate and store token
        tokenService.generateToken(email);
        logger.info("Token generated for email: {}", email);
    }


}
