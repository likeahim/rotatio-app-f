package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.container.button.ButtonContainer;
import com.app.rotatio.vaadin.container.field.FieldContainer;
import com.app.rotatio.vaadin.domain.dto.BackendlessLoginUserDto;
import com.app.rotatio.vaadin.domain.dto.UserDto;
import com.app.rotatio.vaadin.domain.dto.UserRegisterDto;
import com.app.rotatio.vaadin.view.BaseView;
import com.app.rotatio.vaadin.view.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StartViewService {


    private final RestTemplate restTemplate;
    private final String registerEndpoint = "http://localhost:8080/v1/rotatio/users";


    public void logIn(Button loginButton, RestTemplate restTemplate) {
        loginButton.addClickListener(event -> {
            logInUser(restTemplate);
        });
    }

    private static void logInUser(RestTemplate restTemplate) {
        if (!FieldContainer.LOG_IN.isEmpty() && !FieldContainer.PASSWORD.isEmpty()) {
            String login = FieldContainer.LOG_IN.getValue();
            String password = FieldContainer.PASSWORD.getValue();
            BackendlessLoginUserDto backendlessUser = new BackendlessLoginUserDto(login, password, null);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BackendlessLoginUserDto> request = new HttpEntity<>(backendlessUser, headers);

            try {
                ResponseEntity<UserDto> response = restTemplate.exchange(
                        "http://localhost:8080/v1/rotatio/users/login",
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

        public void signIn(Button signInButton, RestTemplate restTemplate) {
            signInButton.addClickListener(event -> { openSignInDialog();
            });
        }

    private void openSignInDialog() {
        Dialog signInDialog = new Dialog();
        Button cancel = ButtonContainer.CANCEL_BUTTON;
        Button submit = ButtonContainer.SUBMIT_BUTTON;
        cancel.addClickListener(event -> signInDialog.close());
        submit.addClickListener(event -> {
            UserRegisterDto userDto = new UserRegisterDto(
                    FieldContainer.SIGN_IN_FIRSTNAME_FIELD.getValue(),
                    FieldContainer.SIGN_IN_LASTNAME_FIELD.getValue(),
                    FieldContainer.SIGN_IN_EMAIL_FIELD.getValue(),
                    FieldContainer.SIGN_IN_PASSWORD_FIELD.getValue()
            );
            registerUser(userDto);
            signInDialog.close();
        });

        signInDialog.add(FieldContainer.SIGN_IN_FIRSTNAME_FIELD, FieldContainer.SIGN_IN_LASTNAME_FIELD,
                FieldContainer.SIGN_IN_EMAIL_FIELD, FieldContainer.SIGN_IN_PASSWORD_FIELD,
                ButtonContainer.SUBMIT_BUTTON, ButtonContainer.CANCEL_BUTTON);

        signInDialog.open();
    }

    private void registerUser(UserRegisterDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRegisterDto> request = new HttpEntity<>(userDto, headers);
        try {
            ResponseEntity<UserRegisterDto> response = restTemplate.postForEntity(registerEndpoint, request, UserRegisterDto.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                Notification.show("User register successfully: " + response.getBody().email(), 3000, Notification.Position.TOP_CENTER);
            } else {
                Notification.show("Registration failed: " + response.getStatusCode(), 3000, Notification.Position.TOP_CENTER);
            }
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }
}
