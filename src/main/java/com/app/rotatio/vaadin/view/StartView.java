package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.container.button.ButtonContainer;
import com.app.rotatio.vaadin.container.field.FieldContainer;
import com.app.rotatio.vaadin.service.StartViewService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

@Route("start")
public class StartView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final StartViewService service;

    public StartView(RestTemplate restTemplate, StartViewService service) {
        this.service = service;
        this.restTemplate = restTemplate;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        service.logIn(ButtonContainer.LOGIN_BUTTON, restTemplate);
        service.signIn(ButtonContainer.SIGN_IN_BUTTON, restTemplate);

        add(
                FieldContainer.LOG_IN,
                FieldContainer.PASSWORD,
                ButtonContainer.LOGIN_BUTTON,
                ButtonContainer.SIGN_IN_BUTTON
        );
    }
}
