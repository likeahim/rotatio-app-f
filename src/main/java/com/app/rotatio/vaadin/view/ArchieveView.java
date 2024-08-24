package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.ArchiveDto;
import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.app.rotatio.vaadin.service.ArchiveViewService;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.PlanViewService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Route("archived-plans")
public class ArchieveView extends BaseView {
    private final ArchiveViewService service;
    private final PlanViewService planService;
    private final Dialog dialog = new Dialog();
    private final Binder<ArchiveDto> binder = new Binder<>(ArchiveDto.class);
    private Grid<ArchiveDto> archivesGrid;
    VerticalLayout rightLayout = new VerticalLayout();

    public ArchieveView(BaseViewService baseViewService, RestTemplate restTemplate,
                        ArchiveViewService service, PlanViewService planService) {
        super(baseViewService, restTemplate);
        this.service = service;
        this.planService = planService;

        VerticalLayout mainContent = baseViewService.createMainContent("Archive");
        setContent(mainContent);

        List<ArchiveDto> archives = service.getAllArchives();
        archivesGrid = createArchivesGrid(archives);
        VerticalLayout leftLayout = baseViewService.createLeftLayout(archivesGrid, getHorizontalLayout(archivesGrid));
        HorizontalLayout splitLayout = service.createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
        service.configureDialog(dialog);
        refreshGrid();
    }

    private HorizontalLayout getHorizontalLayout(Grid<ArchiveDto> archivesGrid) {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        Button showAll = new Button("Show all", event -> refreshGrid());

        DatePicker executeDate = new DatePicker(LocalDate.now());
        Button showByExecuteDate = new Button("Show by date", event -> {
            LocalDate date = executeDate.getValue();
            try {
                WorkingDayDto plan = planService.getPlanByExecuteDay(date);
                ArchiveDto archive = service.getByPlan(plan.getId());
                archivesGrid.setItems(archive);
            } catch (Exception e) {
                Notification.show("There is no archive for " + date, 3000, Notification.Position.MIDDLE);
            }
        });
        filterLayout.add(showAll, executeDate, showByExecuteDate);
        return filterLayout;
    }

    private void refreshGrid() {
        clearRightLayout();
        List<ArchiveDto> archives = service.getAllArchives();
        archivesGrid.setItems(archives);
    }

    private void clearRightLayout() {
        rightLayout.removeAll();
    }

    private Grid<ArchiveDto> createArchivesGrid(List<ArchiveDto> archives) {
        NativeLabel label;
        Grid<ArchiveDto> archivesGrid = new Grid<>(ArchiveDto.class);
        archivesGrid.removeAllColumns();
        if(archives.isEmpty()) {
            label = new NativeLabel("No archive found");
            Notification.show("No archives", 3000, Notification.Position.MIDDLE);
            return archivesGrid;
        }
        archivesGrid.addColumn(ArchiveDto::getId).setHeader("ID").setVisible(false);
        archivesGrid.addColumn(archive -> {
            Long workingDayId = archive.getWorkingDayId();
            WorkingDayDto workingDay = planService.getPlanById(workingDayId);
            return workingDay != null ? workingDay.getExecuteDate() : null;
        }).setHeader("Execute date");
        archivesGrid.addComponentColumn(this::createPrintButton).setHeader("Print");
        archivesGrid.addComponentColumn(this::createDeleteButton).setHeader("Delete");
        archivesGrid.addComponentColumn(this::createShowWorkersButton).setHeader("Show workers");
        archivesGrid.setItems(archives);
        return archivesGrid;
    }

    private Component createShowWorkersButton(ArchiveDto archiveDto) {
        return new Button("Show workers", VaadinIcon.USERS.create(), event -> {
            List<WorkerDto> workers = service.getPlannedWorkers(archiveDto.getWorkingDayId());
            Grid<WorkerDto> workersGrid = getWorkerDtoGrid(workers);
            rightLayout.add(workersGrid);
            if (workers.isEmpty()) {
                Notification.show("No workers planned", 3000, Notification.Position.MIDDLE);
            }
        });
    }

    private static Grid<WorkerDto> getWorkerDtoGrid(List<WorkerDto> workers) {
        Grid<WorkerDto> workersGrid = new Grid<>(WorkerDto.class);
        workersGrid.removeAllColumns();
        workersGrid.addColumn(WorkerDto::getId).setHeader("ID").setVisible(false);
        workersGrid.addColumn(WorkerDto::getFirstName).setHeader("Name");
        workersGrid.addColumn(WorkerDto::getLastname).setHeader("Surname");
        workersGrid.setItems(workers);
        return workersGrid;
    }

    private Component createDeleteButton(ArchiveDto archiveDto) {
        return new Button("Delete", VaadinIcon.TRASH.create(), event -> {
            service.deleteArchive(archiveDto.getId());
            Notification.show("Archive deleted", 3000, Notification.Position.MIDDLE);
            refreshGrid();
        });
    }

    private Component createPrintButton(ArchiveDto archiveDto) {
        return new Button("Print", VaadinIcon.PRINT.create(), event -> {
            service.printArchive(archiveDto);
            Notification.show("Archive printed", 3000, Notification.Position.MIDDLE);
        });
    }
}
