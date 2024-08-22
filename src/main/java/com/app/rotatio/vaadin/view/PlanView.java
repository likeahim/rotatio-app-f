package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.UserDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.PlanViewService;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Route("plans")
public class PlanView extends BaseView {

    private final PlanViewService planService;

    public PlanView(BaseViewService baseViewService, RestTemplate restTemplate, PlanViewService planService) {
        super(baseViewService, restTemplate);
        this.planService = planService;

        VerticalLayout mainContent = createMainContent();
        setContent(mainContent);

        List<WorkingDayDto> workingDays = planService.getWorkingDays();
        Grid<WorkingDayDto> workingDayGrid = createWorkingDayGrid(workingDays);
        VerticalLayout leftLayout = createLeftLayout(workingDayGrid);
        VerticalLayout rightLayout = createRightLayout();

        HorizontalLayout splitLayout = createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
    }

    private VerticalLayout createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);

        NativeLabel titleLabel = new NativeLabel("Plans");
        titleLabel.getStyle()
                .set("font-size", "36px")
                .set("font-weight", "bold");
        mainContent.add(titleLabel);

        return mainContent;
    }

    private Grid<WorkingDayDto> createWorkingDayGrid(List<WorkingDayDto> workingDays) {
        Grid<WorkingDayDto> workingDayGrid = new Grid<>(WorkingDayDto.class);
        workingDayGrid.removeAllColumns();
        workingDayGrid.addColumn(WorkingDayDto::executeDate).setHeader("Execute date");
        workingDayGrid.addColumn(WorkingDayDto::created).setHeader("Created");
        workingDayGrid.addColumn(WorkingDayDto::planned).setHeader("Planned");
        workingDayGrid.addColumn(WorkingDayDto::archived).setHeader("Archived");
        workingDayGrid.addColumn(day -> {
            UserDto user = planService.getUserById(day.userId());
            return user != null ? user.firstName() : "User not found";
        }).setHeader("Made by");
        workingDayGrid.setPageSize(15);
        workingDayGrid.setItems(workingDays);
        return workingDayGrid;
    }

    private VerticalLayout createLeftLayout(Grid<WorkingDayDto> workingDayGrid) {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("70%");
        leftLayout.setPadding(false);
        leftLayout.setSpacing(false);
        leftLayout.add(workingDayGrid);
        return leftLayout;
    }

    private VerticalLayout createRightLayout() {
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("30%");
        rightLayout.setPadding(false);
        rightLayout.setSpacing(false);

        NativeLabel addPlan = new NativeLabel("Add plan");
        FormatMethods.formatFontLabel(addPlan, "32px", "bold");
        rightLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        rightLayout.add(addPlan);

        DatePicker execute = new DatePicker("Execute date");
        DatePicker created = new DatePicker("Create date", LocalDate.now());
        Button createPlan = new Button("Create", event -> {
            LocalDate executeValue = execute.getValue() != null ? execute.getValue() : null;
            LocalDate createdValue = created.getValue() != null ? created.getValue() : LocalDate.now();
            try {
                WorkingDayDto workingDay = planService.createWorkingDay(createdValue, executeValue, BaseView.getUSER_ID());
                Notification.show("New plan created", 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Plan for this day " + executeValue + " exists", 3000, Notification.Position.MIDDLE);
            }
        });
        rightLayout.add(execute, created, createPlan);

        return rightLayout;
    }

    private HorizontalLayout createSplitLayout(VerticalLayout leftLayout, VerticalLayout rightLayout) {
        HorizontalLayout splitLayout = new HorizontalLayout();
        splitLayout.setSizeFull();

        splitLayout.add(leftLayout, rightLayout);

        return splitLayout;
    }
}