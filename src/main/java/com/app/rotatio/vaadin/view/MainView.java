package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.MainViewService;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

@Route("main")
public class MainView extends BaseView {

    private final MainViewService mainViewService;

    public MainView(BaseViewService baseViewService, RestTemplate restTemplate, MainViewService mainViewService) {
        super(baseViewService, restTemplate);
        this.mainViewService = mainViewService;

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setAlignItems(FlexComponent.Alignment.CENTER);
        NativeLabel infoLabel = new NativeLabel("Rotatio App 0.0.1 - plan your workflow with us");
        FormatMethods.formatFontLabel(infoLabel, "24px", "normal");
        mainContent.add(infoLabel);
        int employedWorkers = mainViewService.getEmployedWorkersCount();
        NativeLabel employedLabel = new NativeLabel("Employed workers: " + employedWorkers);
        FormatMethods.formatFontLabel(employedLabel, "32px", "bold");
        mainContent.add(employedLabel);
        int forecastWorkers = mainViewService.getForecastWorkersCount();
        NativeLabel forecastLabel = new NativeLabel("Forecast: " + forecastWorkers);
        FormatMethods.formatFontLabel(forecastLabel, "40px", "bolder");
        mainContent.add(forecastLabel);
        setContent(mainContent);
    }
}
