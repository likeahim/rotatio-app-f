package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.TaskDto;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.TaskViewService;
import com.app.rotatio.vaadin.view.format.FormatMethods;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("tasks")
public class TaskView extends BaseView {
    private final TaskViewService taskService;
    private final Dialog dialog = new Dialog();
    private final Binder<TaskDto> binder = new Binder<>(TaskDto.class);
    private Grid<TaskDto> taskGrid;

    public TaskView(BaseViewService baseViewService, RestTemplate restTemplate, TaskViewService taskService) {
        super(baseViewService, restTemplate);
        this.taskService = taskService;

        VerticalLayout mainContent = createMainContent();
        setContent(mainContent);

        List<TaskDto> tasks = taskService.getAllTasks();
        taskGrid = createTaskGrid(tasks);
        VerticalLayout leftLayout = createLeftLayout(taskGrid);
        VerticalLayout rightLayout = createRightLayout();

        HorizontalLayout splitLayout = createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
        configureDialog();
    }

    private HorizontalLayout createSplitLayout(VerticalLayout leftLayout, VerticalLayout rightLayout) {
        HorizontalLayout splitLayout = new HorizontalLayout();
        splitLayout.setSizeFull();

        splitLayout.add(leftLayout, rightLayout);

        return splitLayout;
    }

    private VerticalLayout createRightLayout() {
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("30%");
        rightLayout.setPadding(false);
        rightLayout.setSpacing(false);

        NativeLabel addTask = new NativeLabel("Add task");
        FormatMethods.formatFontLabel(addTask, "32px", "bold");
        rightLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        rightLayout.add(addTask);

        TextField taskName = new TextField("Task name");
        TextField taskDescription = new TextField("Description");
        Checkbox isPerformed = new Checkbox("Performed");
        Button createTask = new Button("Create", event -> {
            String name = !taskName.isEmpty() ? taskName.getValue() : null;
            String description = !taskDescription.isEmpty() ? taskDescription.getValue() : "";
            boolean performed = isPerformed.getValue();
            try {
                TaskDto taskDto = taskService.createTask(name, description, performed);
                Notification.show("New task created", 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Task creating process failed", 3000, Notification.Position.MIDDLE);
            }
        });
        rightLayout.add(taskName, taskDescription, isPerformed, createTask);

        return rightLayout;
    }

    private VerticalLayout createLeftLayout(Grid<TaskDto> taskGrid) {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("70%");
        leftLayout.setPadding(false);
        leftLayout.setSpacing(false);

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);

        Checkbox performedFilterCheckbox = new Checkbox("is performed");
        Button showButton = new Button("show", event -> {
            boolean isPerformed = performedFilterCheckbox.getValue();
            List<TaskDto> filteredTasks = taskService.getTasksByPerformed(isPerformed);
            taskGrid.setItems(filteredTasks);
        });
        Button showAllButton = new Button("show all", event -> {
            List<TaskDto> allTasks = taskService.getAllTasks();
            taskGrid.setItems(allTasks);
        });

        filterLayout.add(performedFilterCheckbox, showButton, showAllButton);
        filterLayout.getStyle().set("border", "1px solid black").set("padding", "10px");

        leftLayout.add(filterLayout, taskGrid);
        return leftLayout;
    }

    private Grid<TaskDto> createTaskGrid(List<TaskDto> tasks) {
        Grid<TaskDto> taskDtoGrid = new Grid<>(TaskDto.class);
        taskDtoGrid.removeAllColumns();
        taskDtoGrid.addColumn(TaskDto::getName).setHeader("Name");
        taskDtoGrid.addColumn(TaskDto::getDescription).setHeader("Description");
        taskDtoGrid.addColumn(TaskDto::isPerformed).setHeader("Performed");
        taskDtoGrid.addComponentColumn(this::createEditButton).setHeader("Edit");
        taskDtoGrid.setItems(tasks);
        return taskDtoGrid;
    }

    private Component createEditButton(TaskDto taskDto) {
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.addClickListener(event -> openDialog(taskDto));
        return editButton;
    }

    private void openDialog(TaskDto task) {
        TextField nameField = new TextField("Task name");
        TextArea descriptionField = new TextArea("Description");
        Checkbox performedField = new Checkbox("Performed");

        binder.bind(nameField, TaskDto::getName, TaskDto::setName);
        binder.bind(descriptionField, TaskDto::getDescription, TaskDto::setDescription);
        binder.bind(performedField, TaskDto::isPerformed, TaskDto::setPerformed);

        binder.readBean(task);

        Button saveButton = new Button("Save", e -> {
            if (binder.writeBeanIfValid(task)) {
                taskService.updateTask(task);
                refreshGrid();
            }
            dialog.close();
        });

        VerticalLayout dialogLayout = new VerticalLayout(nameField, descriptionField, performedField, saveButton);
        dialog.removeAll();
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void configureDialog() {
        dialog.setWidth("400px");
        dialog.setHeight("300px");
    }

    private VerticalLayout createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);

        NativeLabel titleLabel = new NativeLabel("Tasks");
        titleLabel.getStyle()
                .set("font-size", "36px")
                .set("font-weight", "bold");
        mainContent.add(titleLabel);
        return mainContent;
    }

    private void refreshGrid() {
        List<TaskDto> tasks = taskService.getAllTasks();
        taskGrid.setItems(tasks);
    }
}
