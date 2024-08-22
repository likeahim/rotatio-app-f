package com.app.rotatio.vaadin.view.button;

import com.vaadin.flow.component.UI;
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

    public static Button createPlanButton() {
        return new Button("Create");
    }

    private static void showLogOutDialog() {
        Dialog logOutDialog = new Dialog();
        logOutDialog.add("Are you sure you want to log out?");
        Button confirmButton = new Button("OK", event -> {
            Notification.show("Logging out...");
            logOutDialog.close();
        });
        Button cancelButton = new Button("Cancel", event -> logOutDialog.close());
        logOutDialog.add(confirmButton, cancelButton);
        logOutDialog.open();
    }
}
