package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.ArchiveDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.PlanViewService;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Route("plans")
public class PlanView extends BaseView {

    private final PlanViewService planService;
    private final Dialog dialog = new Dialog();
    private final Binder<WorkingDayDto> binder = new Binder<>(WorkingDayDto.class);
    private Grid<WorkingDayDto> workingDayGrid;

    public PlanView(BaseViewService<WorkingDayDto> baseViewService, RestTemplate restTemplate, PlanViewService planService) {
        super(baseViewService, restTemplate);
        this.planService = planService;

        VerticalLayout mainContent = planService.createMainContent("Plans");
        setContent(mainContent);

        List<WorkingDayDto> workingDays = planService.getAllPlans();
        workingDayGrid = createWorkingDayGrid(workingDays);
        VerticalLayout leftLayout = baseViewService.createLeftLayout(workingDayGrid, getHorizontalLayout(workingDayGrid));
        VerticalLayout rightLayout = createRightLayout();
        HorizontalLayout splitLayout = planService.createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
        planService.configureDialog(dialog);
        refreshGrid();
    }

    private Grid<WorkingDayDto> createWorkingDayGrid(List<WorkingDayDto> workingDays) {
        Grid<WorkingDayDto> workingDayGrid = new Grid<>(WorkingDayDto.class);
        workingDayGrid.removeAllColumns();
        workingDayGrid.addColumn(WorkingDayDto::getExecuteDate).setHeader("Execute date");
        workingDayGrid.addColumn(WorkingDayDto::getCreated).setHeader("Created");
        workingDayGrid.addColumn(WorkingDayDto::getPlanned).setHeader("Planned");
        workingDayGrid.addColumn(WorkingDayDto::getArchived).setHeader("Archived");
        workingDayGrid.addComponentColumn(this::createEditButton).setHeader("Edit");
        workingDayGrid.addComponentColumn(this::createArchiveButton).setHeader("Archive");

        workingDayGrid.setPageSize(15);
        workingDayGrid.setItems(workingDays);
        return workingDayGrid;
    }

    private Component createArchiveButton(WorkingDayDto workingDayDto) {
        Button archiveButton = new Button(VaadinIcon.ARCHIVE.create());
        if (workingDayDto.getArchived()) {
            archiveButton.setVisible(false);
        }
        archiveButton.addClickListener(e -> {
            try {
                ArchiveDto archiveDto = planService.archivePlan(workingDayDto.getId());
                Notification.show("Plan archived successfully", 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } catch (Exception ex) {
                Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        return archiveButton;
    }

    private Component createEditButton(WorkingDayDto workingDayDto) {
        Button editButton = new Button(VaadinIcon.EDIT.create());
        if (workingDayDto.getArchived()) {
            editButton.setVisible(false);
        }
        editButton.addClickListener(event -> openDialog(workingDayDto));
        return editButton;
    }

    private void openDialog(WorkingDayDto workingDay) {
        DatePicker executeDate = new DatePicker("Execute date");
        Checkbox planned = new Checkbox("Planned");
        binder.bind(executeDate, WorkingDayDto::getExecuteDate, WorkingDayDto::setExecuteDate);
        binder.bind(planned, WorkingDayDto::getPlanned, WorkingDayDto::setPlanned);
        binder.readBean(workingDay);


        Button saveButton = new Button("Save", e -> {
            if (binder.writeBeanIfValid(workingDay)) {
                planService.updatePlan(workingDay);
                refreshGrid();
            }
            dialog.close();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(executeDate, planned, saveButton, cancelButton);
        dialog.removeAll();
        dialog.add(dialogLayout);
        dialog.open();
        dialogLayout.setBoxSizing(BoxSizing.CONTENT_BOX);
    }

    private void refreshGrid() {
        List<WorkingDayDto> plans = planService.getAllPlans();
        workingDayGrid.setItems(plans);
    }

    private HorizontalLayout getHorizontalLayout(Grid<WorkingDayDto> workingDayGrid) {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        Button showAllPlans = new Button("Show all plans", event -> refreshGrid());
        Button showUsersPlans = new Button("Show my plans", event -> {
            Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
            List<WorkingDayDto> usersPlans = planService.getUsersPlans(userId);
            workingDayGrid.setItems(usersPlans);
        });
        Button showPlannedPlans = new Button("Show all planned", event -> {
            List<WorkingDayDto> plannedPlans = planService.getPlansByPlanned(true);
            workingDayGrid.setItems(plannedPlans);
        });
        DatePicker executeDate = new DatePicker(LocalDate.now());
        Button showByExecuteDate = new Button("Show by date", event -> {
            LocalDate date = executeDate.getValue();
            try {
                WorkingDayDto planByExecuteDay = planService.getPlanByExecuteDay(date);
                workingDayGrid.setItems(planByExecuteDay);
            } catch (Exception e) {
                Notification.show("There is no plan for " + date, 3000, Notification.Position.MIDDLE);
            }
        });
        filterLayout.add(showUsersPlans, showAllPlans, showPlannedPlans, executeDate, showByExecuteDate);
        return filterLayout;
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
        created.setReadOnly(true);
        Button createPlan = new Button("Create", event -> {
            LocalDate executeValue = execute.getValue() != null ? execute.getValue() : null;
            LocalDate createdValue = created.getValue() != null ? created.getValue() : LocalDate.now();
            try {
                WorkingDayDto workingDay = planService.createPlan(createdValue, executeValue, (Long) VaadinSession.getCurrent().getAttribute("userId"));
                Notification.show("New plan created", 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Plan for this day " + executeValue + " exists", 3000, Notification.Position.MIDDLE);
            }
        });
        rightLayout.add(execute, created, createPlan);

        return rightLayout;
    }


}