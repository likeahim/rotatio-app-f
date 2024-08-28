package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.container.button.ButtonContainer;
import com.app.rotatio.vaadin.container.field.FieldContainer;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartViewService {

    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;

    public void addLoginButtonListener(Button loginButton) {
        loginButton.addClickListener(event -> authenticationService.logInUser());
    }

    public void addSignInButtonListener(Button signInButton) {
        signInButton.addClickListener(event -> openSignInDialog());
    }

    public void addRestorePasswordButtonListener(Button restorePasswordButton) {
        restorePasswordButton.addClickListener(event -> openRestorePasswordDialog());
    }

    private void openSignInDialog() {
        Dialog signInDialog = new Dialog();
        Button cancel = ButtonContainer.CANCEL_BUTTON;
        Button submit = ButtonContainer.SUBMIT_BUTTON;

        cancel.addClickListener(event -> signInDialog.close());
        submit.addClickListener(event -> {
            registrationService.registerUser(
                    FieldContainer.SIGN_IN_FIRSTNAME_FIELD.getValue(),
                    FieldContainer.SIGN_IN_LASTNAME_FIELD.getValue(),
                    FieldContainer.SIGN_IN_EMAIL_FIELD.getValue(),
                    FieldContainer.SIGN_IN_PASSWORD_FIELD.getValue()
            );
            clearFields(FieldContainer.SIGN_IN_FIRSTNAME_FIELD, FieldContainer.SIGN_IN_LASTNAME_FIELD,
                    FieldContainer.SIGN_IN_EMAIL_FIELD, FieldContainer.SIGN_IN_PASSWORD_FIELD);
            signInDialog.close();
        });

        signInDialog.add(
                FieldContainer.SIGN_IN_FIRSTNAME_FIELD,
                FieldContainer.SIGN_IN_LASTNAME_FIELD,
                FieldContainer.SIGN_IN_EMAIL_FIELD,
                FieldContainer.SIGN_IN_PASSWORD_FIELD,
                submit,
                cancel
        );
        signInDialog.open();
    }

    private void openRestorePasswordDialog() {
        Dialog restoreDialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();
        NativeLabel label = new NativeLabel("Click submit to receive \n restore password email");
        Button cancel = ButtonContainer.CANCEL_BUTTON;
        Button submit = ButtonContainer.SUBMIT_BUTTON;
        TextField emailField = new TextField("Email");

        cancel.addClickListener(event -> restoreDialog.close());
        submit.addClickListener(event -> {
            authenticationService.restorePassword(emailField.getValue());
            restoreDialog.close();
        });

        layout.add(label, emailField, submit, cancel);
        restoreDialog.add(layout);
        restoreDialog.open();
    }

    private void clearFields(HasValue<?, ?>... fields) {
        for (HasValue<?, ?> field : fields) {
            field.clear();
        }
    }
}