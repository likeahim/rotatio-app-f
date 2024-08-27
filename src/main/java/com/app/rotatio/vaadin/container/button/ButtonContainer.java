package com.app.rotatio.vaadin.container.button;

import com.app.rotatio.vaadin.view.button.ButtonHelper;
import com.vaadin.flow.component.button.Button;

public class ButtonContainer {

    public static final Button LOGIN_BUTTON = new Button("Log in");
    public static final Button SIGN_IN_BUTTON = new Button("Sign up");
    public static final Button LOG_OUT = ButtonHelper.createLogOutButton();
    public static final Button SUBMIT_BUTTON = new Button("Submit");
    public static final Button CANCEL_BUTTON = new Button("Cancel");
    public static final Button RESTORE_PASSWORD = new Button("Restore password");
}
