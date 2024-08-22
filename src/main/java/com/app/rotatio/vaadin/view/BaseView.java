package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.view.button.ButtonHelper;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

@Route("basic")
public class BaseView extends AppLayout implements RouterLayout {

    private final BaseViewService baseViewService;
    @Setter
    @Getter
    public static Long USER_ID;
    private final RestTemplate restTemplate;

    public BaseView(BaseViewService baseViewService, RestTemplate restTemplate) {
        this.baseViewService = baseViewService;

        Button logOutButton = ButtonHelper.createLogOutButton();
        baseViewService.logOut(logOutButton, restTemplate);
        
        H1 title = new H1("R O T A T I O");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "20");
        
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        headerLayout.add(new DrawerToggle(), title);
        headerLayout.expand(title);
        headerLayout.add(logOutButton);
        
        addToNavbar(headerLayout);
        
        Tabs tabs = baseViewService.getTabs();
        addToDrawer(tabs);
        
        NativeLabel label = new NativeLabel("Loading...");
        label.getStyle().set("margin-top", "auto");  // Ustawienie automatycznego marginesu, aby było na dole
        addToDrawer(label);
        label.setText(baseViewService.getTime());
        this.restTemplate = restTemplate;
    }
}
