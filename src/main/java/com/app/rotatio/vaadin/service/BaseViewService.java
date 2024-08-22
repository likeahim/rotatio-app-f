package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.view.MainView;
import com.app.rotatio.vaadin.view.StartView;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static javax.swing.UIManager.getUI;

@Service
public class BaseViewService {

    public final RestTemplate restTemplate;

    public BaseViewService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTime() {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/time/value?timeZone=Poland", String.class
        );
    }

    public Tabs getTabs() {
        Tabs tabs = new Tabs();

        Tab home = createTab(VaadinIcon.HOME, "Home");
        Tab workplaces = createTab(VaadinIcon.WORKPLACE, "Workplaces");
        Tab tasks = createTab(VaadinIcon.TASKS, "Tasks");
        Tab userData = createTab(VaadinIcon.USER, "User Data");
        Tab workers = createTab(VaadinIcon.RECORDS, "Workers");
        Tab plans = createTab(VaadinIcon.LIST, "Plans");
        FormatMethods.setNavigate(home, "main");
        FormatMethods.setNavigate(workplaces, "workplaces");
        FormatMethods.setNavigate(plans, "plans");
        FormatMethods.setNavigate(tasks, "tasks");
        FormatMethods.setNavigate(workers, "workers");
        FormatMethods.setNavigate(userData, "user-data");
        tabs.add(home, plans, workers, workplaces, tasks, userData);

        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    public Tab createTab(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setTabIndex(-1);

        return new Tab(link);
    }

    public void logOut(Button logOutButton, RestTemplate restTemplate) {
        logOutButton.addClickListener(event -> {
            Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
            String userToken = (String) VaadinSession.getCurrent().getAttribute("userToken");
            String url = "";
            if (userId != null) {
                url = "http://localhost:8080/v1/rotatio/users/logout/" + userId;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("user-token", userToken);
            HttpEntity<String> request = new HttpEntity<>(headers);
            try {
                ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, request, Void.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    VaadinSession.getCurrent().setAttribute("userId", null);
                    VaadinSession.getCurrent().setAttribute("userToken", null);
                    Notification.show("Logged out successfully.");
                    UI.getCurrent().close();
                    VaadinSession.getCurrent().close();
                } else {
                    Notification.show("Failed to logout. Please try again.", 5000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("An error occurred during logout: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });
    }
}
