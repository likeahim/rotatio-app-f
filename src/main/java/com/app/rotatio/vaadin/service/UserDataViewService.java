package com.app.rotatio.vaadin.service;

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
    public UserDataViewService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public UserDto getUser(Long userId) {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/users/" + userId,
                UserDto.class
        );
    }

    public List<UserDto> getAllUsers() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/users",
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
                    "http://localhost:8080/v1/rotatio/users",
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
                "http://localhost:8080/v1/rotatio/users" + objectId
        );
    }
}

