package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.UserDto;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserDataViewService extends BaseViewService {

    public UserDataViewService(RestTemplate restTemplate, EndpointConfig endpointConfig) {
        super(restTemplate, endpointConfig);
    }

    public UserDto getUser(Long userId) {
        return restTemplate.getForObject(
                endpointConfig.getUsersProdEndpoint() + "/" + userId,
                UserDto.class
        );
    }

    public UserDto getUserByEmail(final String email) {
//        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        return restTemplate.getForObject(
                endpointConfig.getUsersByEmailEndpoint() + email,
                UserDto.class
        );
    }

    public List<UserDto> getAllUsers() {
        return restTemplate.exchange(
                endpointConfig.getUsersProdEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {
                }
        ).getBody();
    }

    public void updateUser(UserDto user) {
        HttpHeaders headers = new HttpHeaders();
        String userToken = (String) VaadinSession.getCurrent().getAttribute("userToken");
        if (userToken == null) {
            Notification.show("User token is missing");
            return;
        }
        headers.add("user-token", userToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<UserDto> response = restTemplate.exchange(
                    endpointConfig.getUsersProdEndpoint(),
                    HttpMethod.PUT,
                    request,
                    UserDto.class
            );
            Notification.show("User updated successfully!");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            Notification.show("Failed to update user: " + e.getStatusCode());
        } catch (ResourceAccessException e) {
            Notification.show("Failed to connect to server: " + e.getMessage());
        } catch (Exception e) {
            Notification.show("An unexpected error occurred: " + e.getMessage());
        }
    }


    public void deleteAccount(Long userId) {
        UserDto user = getUser(userId);
        String objectId = user.getObjectId();
        restTemplate.delete(
                endpointConfig.getUsersProdEndpoint() + "/" + objectId
        );
    }

    public void restorePassword(String email) {
        restTemplate.getForEntity(
                endpointConfig.getUsersRestorePasswordEndpoint() + email,
                Void.class
        );
    }
}

