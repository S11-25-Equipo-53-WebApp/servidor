package com.webapp.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WhatsAppConfig {

    @Value("${whatsapp.api.base-url}")
    private String baseUrl;

    @Value("${whatsapp.api.token}")
    private String token;

    @Getter
    @Value("${whatsapp.api.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.verify.token}")
    private String verifyToken;

    @Bean
    public WebClient whatsappWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    @Bean
    public  String phoneNumberId() {
        return phoneNumberId;
    }
    @Bean
    public String verifyToken() {
        return verifyToken;
    }
}
