package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.WorkplaceDto;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.WorkplaceViewService;
import com.app.rotatio.vaadin.view.button.ButtonHelper;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("workplaces")
public class WorkplaceView extends BaseView {

    private final WorkplaceViewService service;
    private final Dialog dialog = new Dialog();
    private final Binder<WorkplaceDto> binder = new Binder<>(WorkplaceDto.class);
    private Grid<WorkplaceDto> workplaceGrid;

    public WorkplaceView(BaseViewService baseViewService, RestTemplate restTemplate, WorkplaceViewService service) {
        super(baseViewService, restTemplate);
        this.service = service;

        VerticalLayout mainContent = baseViewService.createMainContent("Workplace");
        setContent(mainContent);

        List<WorkplaceDto> workplaces = service.getAllWorkplaces();
        workplaceGrid = createWorkplaceGrid(workplaces);
        VerticalLayout leftLayout = createLeftLayout(workplaceGrid);
        VerticalLayout rightLayout = createRightLayout();

        HorizontalLayout splitLayout = service.createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
        service.configureDialog(dialog);
        refreshGrid();
    }

    private VerticalLayout createLeftLayout(Grid<WorkplaceDto> workplaceGrid) {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("70%");
        leftLayout.setPadding(false);
        leftLayout.setSpacing(false);

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);

        Checkbox activeFilterCheckbox = new Checkbox("active");
        Button showByActiveButton = new Button("Show active", event -> {
            boolean active = activeFilterCheckbox.getValue();
            List<WorkplaceDto> filteredWorkplaces = service.getWorkplacesByActive(active);
            workplaceGrid.setItems(filteredWorkplaces);
        });

        Checkbox usedFilterCheckbox = new Checkbox("used");
        Button showByUsedButton = new Button("Show used", event -> {
            boolean used = usedFilterCheckbox.getValue();
            List<WorkplaceDto> filteredWorkplaces = service.getWorkplacesByUsed(used);
            workplaceGrid.setItems(filteredWorkplaces);
        });

        Button showAllButton = new Button("Show all", event -> {
            List<WorkplaceDto> allTasks = service.getAllWorkplaces();
            workplaceGrid.setItems(allTasks);
        });

        filterLayout.add(activeFilterCheckbox, showByActiveButton, usedFilterCheckbox, showByUsedButton, showAllButton);
        filterLayout.getStyle().set("border", "1px solid black").set("padding", "10px");

        leftLayout.add(filterLayout, workplaceGrid);
        return leftLayout;
    }

    private Grid<WorkplaceDto> createWorkplaceGrid(List<WorkplaceDto> workplaces) {
        Grid<WorkplaceDto> workplaceDtoGrid = new Grid<>(WorkplaceDto.class);
        workplaceDtoGrid.removeAllColumns();
        workplaceDtoGrid.addColumn(WorkplaceDto::getId).setHeader("ID").setVisible(false);
        workplaceDtoGrid.addColumn(WorkplaceDto::getDesignation).setHeader("Designation");
        workplaceDtoGrid.addColumn(WorkplaceDto::isActive).setHeader("Active");
        workplaceDtoGrid.addColumn(WorkplaceDto::isNowUsed).setHeader("Now used");
        workplaceDtoGrid.addComponentColumn(this::createEditButton).setHeader("Edit");
        workplaceDtoGrid.setItems(workplaces);
        return workplaceDtoGrid;
    }

    private Component createEditButton(WorkplaceDto workplaceDto) {
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickListener(event -> openDialog(workplaceDto));
        return editButton;
    }

    private void openDialog(WorkplaceDto workplaceDto) {
        TextField designationField = new TextField("Designation");
        Checkbox activeField = new Checkbox("Active");
        Checkbox nowUsedField = new Checkbox("Now used");
        TextField idField = new TextField("id");
        idField.setVisible(false);

        binder.bind(designationField, WorkplaceDto::getDesignation, WorkplaceDto::setDesignation);
        binder.bind(activeField, WorkplaceDto::isActive, WorkplaceDto::setActive);
        binder.bind(nowUsedField, WorkplaceDto::isNowUsed, WorkplaceDto::setNowUsed);
        binder.forField(idField)
                .withConverter(
                        new StringToLongConverter("Invalid ID"))
                .bind(WorkplaceDto::getId, WorkplaceDto::setId);
        binder.readBean(workplaceDto);


        Button saveButton = new Button("Save", e -> {
            if (binder.writeBeanIfValid(workplaceDto)) {
                service.updateWorkplace(workplaceDto);
                refreshGrid();
            }
            dialog.close();
        });

        Button cancelButton = ButtonHelper.createDialogCancelButton(dialog);

        VerticalLayout dialogLayout = new VerticalLayout(designationField, activeField, nowUsedField, saveButton, cancelButton);
        dialog.removeAll();
        dialog.add(dialogLayout);
        dialog.open();
        dialogLayout.setBoxSizing(BoxSizing.CONTENT_BOX);
    }

    private VerticalLayout createRightLayout() {
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("30%");
        rightLayout.setPadding(false);
        rightLayout.setSpacing(false);

        NativeLabel addWorkplace = new NativeLabel("Add workplace");
        FormatMethods.formatFontLabel(addWorkplace, "32px", "bold");
        rightLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        rightLayout.add(addWorkplace);

        TextField workplaceDesignation = new TextField("Destination");
        Checkbox active = new Checkbox("Active", true);
        Checkbox nowUsed = new Checkbox("Now used", true);
        Button createWorkplace = new Button("Create", event -> {
            String name = !workplaceDesignation.isEmpty() ? workplaceDesignation.getValue() : "unnamed";
            boolean isActive = active.getValue();
            boolean used = nowUsed.getValue();
            try {
                WorkplaceDto workplaceDto = service.createWorkplace(name, isActive, used);
                Notification.show("New workplace created: " + name, 3000, Notification.Position.MIDDLE);
                refreshGrid();
                workplaceDesignation.clear();
            } catch (Exception e) {
                Notification.show("Workplace creating process failed", 3000, Notification.Position.MIDDLE);
            }
        });
        rightLayout.add(workplaceDesignation, active, nowUsed, createWorkplace);
        return rightLayout;
    }

    private void refreshGrid() {
        List<WorkplaceDto> workplaces = service.getAllWorkplaces();
        workplaceGrid.setItems(workplaces);
    }
}
