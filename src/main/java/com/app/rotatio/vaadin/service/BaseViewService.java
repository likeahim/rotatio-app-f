package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.app.rotatio.vaadin.view.MainView;
import com.app.rotatio.vaadin.view.StartView;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

import java.util.stream.Stream;

import static javax.swing.UIManager.getUI;

@Service
public class BaseViewService<T> {

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
        Tab archivedPlans = createTab(VaadinIcon.ARCHIVE, "Archived Plans");
        FormatMethods.setNavigate(home, "main");
        FormatMethods.setNavigate(workplaces, "workplaces");
        FormatMethods.setNavigate(plans, "plans");
        FormatMethods.setNavigate(tasks, "tasks");
        FormatMethods.setNavigate(workers, "workers");
        FormatMethods.setNavigate(userData, "user-data");
        FormatMethods.setNavigate(archivedPlans, "archived-plans");
        tabs.add(home, plans, workers, workplaces, tasks, userData, archivedPlans);

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

    public VerticalLayout createMainContent(String title) {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);

        NativeLabel titleLabel = new NativeLabel(title);
        titleLabel.getStyle()
                .set("font-size", "36px")
                .set("font-weight", "bold");
        mainContent.add(titleLabel);
        return mainContent;
    }

    public HorizontalLayout createSplitLayout(VerticalLayout leftLayout, VerticalLayout rightLayout) {
        HorizontalLayout splitLayout = new HorizontalLayout();
        splitLayout.setSizeFull();
        splitLayout.add(leftLayout, rightLayout);
        return splitLayout;
    }

    public VerticalLayout createLeftLayout(Grid<T> grid, HorizontalLayout layout) {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("70%");
        leftLayout.setPadding(false);
        leftLayout.setSpacing(false);
        leftLayout.add(grid);
        layout.getStyle().set("border", "1px solid black").set("padding", "10px");
        leftLayout.add(layout, grid);
        return leftLayout;
    }

    public void configureDialog(Dialog dialog) {
        dialog.setWidth("400px");
        dialog.setHeight("300px");
    }

//to transfer in other service?
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
                    UI.getCurrent().navigate("");
                } else {
                    Notification.show("Failed to logout. Please try again.", 5000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("An error occurred during logout: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });
    }
}
