package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.container.field.FieldContainer;
import com.app.rotatio.vaadin.domain.dto.BackendlessLoginUserDto;
import com.app.rotatio.vaadin.domain.dto.UserDto;
import com.app.rotatio.vaadin.view.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RestTemplate restTemplate;
    private final UserDataViewService userDataViewService;
    private final EndpointConfig endpointConfig;

    public void logInUser() {
        if (!FieldContainer.LOG_IN.isEmpty() && !FieldContainer.PASSWORD.isEmpty()) {
            String login = FieldContainer.LOG_IN.getValue();
            String password = FieldContainer.PASSWORD.getValue();
            BackendlessLoginUserDto backendlessUser = new BackendlessLoginUserDto(login, password, null);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BackendlessLoginUserDto> request = new HttpEntity<>(backendlessUser, headers);

            try {
                ResponseEntity<UserDto> response = restTemplate.exchange(
                        endpointConfig.getUsersLoginEndpoint(),
                        HttpMethod.POST,
                        request,
                        UserDto.class
                );

                VaadinSession.getCurrent().setAttribute("userId", response.getBody().getId());
                VaadinSession.getCurrent().setAttribute("userToken", response.getBody().getUserToken());
                FieldContainer.LOG_IN.clear();
                FieldContainer.PASSWORD.clear();
                Notification.show("Logged in with user: " + response.getBody().getEmail(), 3000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate(MainView.class);
            } catch (Exception e) {
                Notification.show("Log in failed: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        } else {
            Notification.show("Please fill both fields", 3000, Notification.Position.TOP_CENTER);
        }
    }

    public void restorePassword(String email) {
        try {
            userDataViewService.restorePassword(email);
            Notification.show("Restore email has been sent, check your mailbox and follow instructions",
                    3000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }
}
