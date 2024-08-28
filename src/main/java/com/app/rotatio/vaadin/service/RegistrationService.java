package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.UserRegisterDto;
import com.vaadin.flow.component.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RestTemplate restTemplate;
    private final EndpointConfig endpointConfig;

    public void registerUser(String firstName, String lastName, String email, String password) {
        UserRegisterDto userDto = new UserRegisterDto(firstName, lastName, email, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRegisterDto> request = new HttpEntity<>(userDto, headers);

        try {
            ResponseEntity<UserRegisterDto> response = restTemplate.postForEntity(
                    endpointConfig.getUsersProdEndpoint(),
                    request,
                    UserRegisterDto.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                Notification.show("User registered successfully: " + response.getBody().email(), 3000, Notification.Position.TOP_CENTER);
            } else {
                Notification.show("Registration failed: " + response.getStatusCode(), 3000, Notification.Position.TOP_CENTER);
            }
        } catch (Exception e) {
        }
    }
}
