package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.WorkerStatus;
import com.app.rotatio.vaadin.domain.dto.*;
import com.app.rotatio.vaadin.service.*;
import com.app.rotatio.vaadin.view.button.ButtonHelper;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route("workers")
public class WorkerView extends BaseView {

    private final WorkerViewService service;
    private final TaskViewService taskViewService;
    private final WorkplaceViewService workplaceViewService;
    private final PlanViewService planViewService;
    private final Dialog dialog = new Dialog();
    private final Binder<WorkerDto> binder = new Binder<>(WorkerDto.class);
    private Grid<WorkerDto> workersGrid;
    private Long workerNumber;

    public WorkerView(BaseViewService baseViewService, RestTemplate restTemplate, WorkerViewService service, TaskViewService taskViewService, WorkplaceViewService workplaceViewService, PlanViewService planViewService) {
        super(baseViewService, restTemplate);
        this.service = service;
        this.taskViewService = taskViewService;
        this.workplaceViewService = workplaceViewService;
        this.planViewService = planViewService;

        VerticalLayout mainContent = baseViewService.createMainContent("Workers");
        setContent(mainContent);

        List<WorkerDto> workers = service.getAllWorkers();
        workersGrid = createWorkerGrid(workers);
        VerticalLayout leftLayout = createLeftLayout(workersGrid);
        VerticalLayout rightLayout = createRightLayout();

        HorizontalLayout splitLayout = service.createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
        service.configureDialog(dialog);
        refreshGrid();
    }

    private VerticalLayout createLeftLayout(Grid<WorkerDto> workersGrid) {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("70%");
        leftLayout.setPadding(false);
        leftLayout.setSpacing(false);

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);

        Button showAll = new Button("Show All", event -> {
            refreshGrid();
        });

        DatePicker presenceTo = new DatePicker("Presence to");
        Button showByPresence = new Button("Presence to", event -> {
            List<WorkerDto> filteredWorkers = service.getWorkersByPresenceTo(presenceTo.getValue());
            workersGrid.setItems(filteredWorkers);
        });

        DatePicker presenceFrom = new DatePicker("Presence from");
        Button showByAbsence = new Button("Present from", event -> {
            List<WorkerDto> filteredWorkers = service.getWorkersByPresenceFrom(presenceFrom.getValue());
            workersGrid.setItems(filteredWorkers);
        });

        DatePicker planDate = new DatePicker("Plan date");
        Button showByPlan = new Button("Show by plan", event -> {
            List<WorkerDto> filteredWorkers = service.getWorkersByPlan(planDate.getValue());
            workersGrid.setItems(filteredWorkers);
        });

        ComboBox<WorkerStatus> status = new ComboBox<>("Status");
        status.setItems(WorkerStatus.getPossibleForUsers());
        Button showByStatus = new Button("Show by status", event -> {
            List<WorkerDto> filteredWorkers = service.getWorkersByStatus(status.getValue().getValue());
            workersGrid.setItems(filteredWorkers);
        });

        filterLayout.add(
                showAll,
                planDate, showByPlan,
                presenceTo, showByPresence,
                presenceFrom, showByAbsence,
                status, showByStatus
        );
        filterLayout.getStyle().set("border", "1px solid black").set("padding", "10px");
        leftLayout.add(filterLayout, workersGrid);
        return leftLayout;
    }

    private VerticalLayout createRightLayout() {
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("30%");
        rightLayout.setPadding(false);
        rightLayout.setSpacing(false);

        NativeLabel addWorker = new NativeLabel("Add worker");
        FormatMethods.formatFontLabel(addWorker, "32px", "bold");
        rightLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        rightLayout.add(addWorker);

        TextField workerNumberField = new TextField("Worker number");

        workerNumberField.addValueChangeListener(event -> {
            try {
                workerNumber = Long.parseLong(event.getValue());
            } catch (NumberFormatException e) {
                Notification.show("Invalid input, please enter a valid worker number.");
            }
        });
        TextField firstName = new TextField("First name");
        TextField lastname = new TextField("Surname");
        ComboBox<WorkerStatus> status = new ComboBox<>("Status");
        status.setItems(WorkerStatus.getPossibleForUsers());

        Button createWorker = new Button("Create", event -> {
            try {
                WorkerDto worker = new WorkerDto(null, workerNumber, firstName.getValue(),
                        lastname.getValue(), status.getValue().getValue(),
                        null, null, null);
                WorkerDto createdWorker = service.createWorker(worker);
                Notification.show("New worker created: " + firstName.getValue() + " " + lastname.getValue(), 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Worker creating process failed", 3000, Notification.Position.MIDDLE);
            }
        });

        Button clearButton = getButton(workerNumberField, firstName, lastname, status);
        rightLayout.add(workerNumberField, firstName, lastname, status, createWorker, clearButton);
        rightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return rightLayout;
    }

    private static Button getButton(TextField workerNumberField, TextField firstName,
                                    TextField lastname, ComboBox<WorkerStatus> status) {
        Button clearButton = new Button("Clear", event -> {
            firstName.clear();
            lastname.clear();
            status.clear();
        });
        return clearButton;
    }

    private Grid<WorkerDto> createWorkerGrid(List<WorkerDto> workers) {
        List<TaskDto> allTasks = taskViewService.getTasksByPerformed(true);
        List<WorkplaceDto> allWorkplaces = workplaceViewService.getWorkplacesByActive(true);
        List<WorkingDayDto> unplannedPlans = planViewService.getPlansByArchived(false);
        Grid<WorkerDto> grid = new Grid<>(WorkerDto.class);
        grid.removeAllColumns();
        grid.addColumn(WorkerDto::getFirstName).setHeader("Name").setSortable(true);
        grid.addColumn(WorkerDto::getLastname).setHeader("Surname").setSortable(true);
        grid.addColumn(WorkerDto::getWorkerNumber).setHeader("Number");
        grid.addComponentColumn(worker -> createPlanComboBox(worker, unplannedPlans)).setHeader("Plan");
        grid.addComponentColumn(worker -> createTaskComboBox(worker, allTasks)).setHeader("Task");
        grid.addComponentColumn(worker -> createWorkplaceComboBox(worker, allWorkplaces)).setHeader("Workplace");
        grid.addComponentColumn(this::createEditButton).setHeader("Edit");
        grid.addComponentColumn(this::createDeleteButton).setHeader("Delete");
        grid.setItems(workers);
        return grid;
    }

    private ComboBox<LocalDate> createPlanComboBox(WorkerDto worker, List<WorkingDayDto> plans) {
        ComboBox<LocalDate> planComboBox = new ComboBox<>();
        if (worker.getStatus() == 3) {
            planComboBox.setVisible(false);
        }
        List<WorkingDayDto> unplannedPlans = plans.stream()
                .filter(p -> !p.getPlanned())
                .toList();
        Map<Long, LocalDate> planMap = unplannedPlans.stream()
                .collect(Collectors.toMap(WorkingDayDto::getId, WorkingDayDto::getExecuteDate));
        planComboBox.setItems(planMap.values());
        planComboBox.setValue(planMap.get(worker.getWorkingDayId()));

        planComboBox.addValueChangeListener(event -> {
            LocalDate date = event.getValue();
            Long selectedPlanId = plans.stream()
                    .filter(plan -> plan.getExecuteDate().equals(date))
                    .findFirst()
                    .map(WorkingDayDto::getId)
                    .orElse(null);

            worker.setWorkingDayId(selectedPlanId);
            service.updateWorker(worker);
        });
        return planComboBox;
    }

    private ComboBox<String> createWorkplaceComboBox(WorkerDto worker, List<WorkplaceDto> availableWorkplaces) {
        ComboBox<String> comboBox = new ComboBox<>();
        if (worker.getStatus() == 3) {
            comboBox.setVisible(false);
        }
        Map<Long, String> workplaceMap = availableWorkplaces.stream()
                .collect(Collectors.toMap(WorkplaceDto::getId, WorkplaceDto::getDesignation));
        comboBox.setItems(workplaceMap.values());
        comboBox.setValue(workplaceMap.get(worker.getWorkplaceId()));

        comboBox.addValueChangeListener(event -> {
            String selectedWorkplaceName = event.getValue();
            Long selectedWorkplaceId = availableWorkplaces.stream()
                    .filter(workplace -> workplace.getDesignation().equals(selectedWorkplaceName))
                    .findFirst()
                    .map(WorkplaceDto::getId)
                    .orElse(null);

            worker.setWorkplaceId(selectedWorkplaceId);
            service.updateWorker(worker);
        });

        return comboBox;
    }

    private ComboBox<String> createTaskComboBox(WorkerDto worker, List<TaskDto> availableTasks) {
        ComboBox<String> comboBox = new ComboBox<>();
        if (worker.getStatus() == 3) {
            comboBox.setVisible(false);
        }
        Map<Long, String> taskMap = availableTasks.stream()
                .collect(Collectors.toMap(TaskDto::getId, TaskDto::getName));
        comboBox.setItems(taskMap.values());
        comboBox.setValue(taskMap.get(worker.getTaskId()));

        comboBox.addValueChangeListener(event -> {
            String selectedTaskName = event.getValue();
            Long selectedTaskId = availableTasks.stream()
                    .filter(task -> task.getName().equals(selectedTaskName))
                    .findFirst()
                    .map(TaskDto::getId)
                    .orElse(null);

            worker.setTaskId(selectedTaskId);
            service.updateWorker(worker);
        });

        return comboBox;
    }


    private Component createDeleteButton(WorkerDto workerDto) {
        Button delete = new Button("Delete", VaadinIcon.TRASH.create());
        if (workerDto.getStatus() == 3) {
            delete.setVisible(false);
        }
        delete.addClickListener(event -> {
            String workersData = workerDto.getFirstName() + " " + workerDto.getLastname();
            try {
                service.deleteWorker(workerDto.getId());
                Notification.show("Worker deleted: " + workersData, 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Worker deletion failed", 3000, Notification.Position.MIDDLE);
            }
            refreshGrid();
        });
        return delete;
    }

    private Component createEditButton(WorkerDto worker) {
        Button editButton = new Button(VaadinIcon.EDIT.create());
        if (worker.getStatus() == 3) {
            editButton.setVisible(false);
        }
        editButton.addClickListener(event -> openDialog(worker));
        return editButton;
    }

    private void openDialog(WorkerDto worker) {

        TextField firstName = new TextField("Name");
        TextField lastname = new TextField("Surname");
        ComboBox<WorkerStatus> status = new ComboBox<>("Status");
        status.setItems(WorkerStatus.getPossibleForUsers());
        TextField idField = new TextField("id");
        idField.setVisible(false);

        binder.bind(firstName, WorkerDto::getFirstName, WorkerDto::setFirstName);
        binder.bind(lastname, WorkerDto::getLastname, WorkerDto::setLastname);
        binder.forField(status)
                .withConverter(new Converter<WorkerStatus, Integer>() {
                    @Override
                    public Result<Integer> convertToModel(WorkerStatus value, ValueContext context) {
                        return Result.ok(value != null ? value.getValue() : null);
                    }

                    @Override
                    public WorkerStatus convertToPresentation(Integer value, ValueContext context) {
                        return value != null ? WorkerStatus.fromValue(value) : null;
                    }
                })
                .bind(WorkerDto::getStatus, WorkerDto::setStatus);
        binder.forField(idField)
                .withConverter(
                        new StringToLongConverter("Invalid ID"))
                .bind(WorkerDto::getId, WorkerDto::setId);
        binder.readBean(worker);


        Button saveButton = new Button("Save", e -> {
            if (binder.writeBeanIfValid(worker)) {
                service.updateWorker(worker);
                refreshGrid();
            }
            dialog.close();
        });

        Button cancelButton = ButtonHelper.createDialogCancelButton(dialog);

        VerticalLayout dialogLayout = new VerticalLayout(firstName, lastname, status,
                saveButton, cancelButton);
        dialog.removeAll();
        dialog.add(dialogLayout);
        dialog.open();
        dialogLayout.setBoxSizing(BoxSizing.CONTENT_BOX);
    }

    private void refreshGrid() {
        List<WorkerDto> workers = service.getAllWorkers();
        workersGrid.setItems(workers);
    }
}