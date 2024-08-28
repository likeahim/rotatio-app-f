package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.container.button.ButtonContainer;
import com.app.rotatio.vaadin.container.field.FieldContainer;
import com.app.rotatio.vaadin.service.StartViewService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class StartView extends VerticalLayout {

    private final StartViewService startViewService;

    public StartView(StartViewService startViewService) {
        this.startViewService = startViewService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        startViewService.addLoginButtonListener(ButtonContainer.LOGIN_BUTTON);
        startViewService.addSignInButtonListener(ButtonContainer.SIGN_IN_BUTTON);
        startViewService.addRestorePasswordButtonListener(ButtonContainer.RESTORE_PASSWORD);

        add(
                FieldContainer.LOG_IN,
                FieldContainer.PASSWORD,
                ButtonContainer.LOGIN_BUTTON,
                ButtonContainer.SIGN_IN_BUTTON,
                ButtonContainer.RESTORE_PASSWORD
        );
    }
}