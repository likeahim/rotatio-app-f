package com.app.rotatio.vaadin.view.button;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.web.client.RestTemplate;

public class ButtonHelper {

    private final RestTemplate restTemplate;

    public ButtonHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static Button createLogOutButton() {
        return new Button("Log out", event ->{ showLogOutDialog();
        });
    }

    private static void showLogOutDialog() {
        Dialog logOutDialog = new Dialog();
        logOutDialog.add("Login out in progress, see you soon");
        Button confirmButton = new Button("OK", event -> {
            Notification.show("Logging out...");
            logOutDialog.close();
        });
        Button cancelButton = new Button("Cancel", event -> logOutDialog.close());
        logOutDialog.add(confirmButton, cancelButton);
        logOutDialog.open();
    }

    public static Button createDialogCancelButton(Dialog dialog) {
        return new Button("Cancel", e -> dialog.close());
    }
}
