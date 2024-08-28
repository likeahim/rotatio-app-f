package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.domain.dto.UserDto;
import com.app.rotatio.vaadin.service.BaseViewService;
import com.app.rotatio.vaadin.service.UserDataViewService;
import com.app.rotatio.vaadin.view.button.ButtonHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("user-data")
public class UserDataView extends BaseView {

    private final UserDataViewService service;
    private final Dialog dialog = new Dialog();
    private final Binder<UserDto> binder = new Binder<>(UserDto.class);
    private Grid<UserDto> usersGrid;
    private VerticalLayout rightLayout = new VerticalLayout();


    public UserDataView(BaseViewService<UserDto> baseViewService, RestTemplate restTemplate, UserDataViewService service) {
        super(baseViewService, restTemplate);
        this.service = service;

        VerticalLayout mainContent = baseViewService.createMainContent("User Data");
        setContent(mainContent);

        List<UserDto> allUsers = service.getAllUsers();
        usersGrid = createUserDtoGrid(allUsers);
        VerticalLayout leftLayout = baseViewService.createLeftLayout(usersGrid, getHorizontalLayout(usersGrid));
        leftLayout.setWidth("100%");
        HorizontalLayout splitLayout = baseViewService.createSplitLayout(leftLayout, rightLayout);
        mainContent.add(splitLayout);
        service.configureDialog(dialog);
        refreshGrid();
    }

    private HorizontalLayout getHorizontalLayout(Grid<UserDto> usersGrid) {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        Button editButton = new Button("Edit my data", clickEvent -> {
            UserDto user = service.getUser((Long) VaadinSession.getCurrent().getAttribute("userId"));
            usersGrid.setItems(user);
            openDialog(user);
        });
        Button deleteButton = new Button("Delete my account", clickEvent -> {
            service.deleteAccount((Long) VaadinSession.getCurrent().getAttribute("userId"));
            UI.getCurrent().navigate("");
        });
        filterLayout.add(editButton, deleteButton);
        return filterLayout;
    }

    private Grid<UserDto> createUserDtoGrid(List<UserDto> allUsers) {
        Grid<UserDto> usersGrid = new Grid<>(UserDto.class);
        usersGrid.removeAllColumns();
        usersGrid.addColumn(UserDto::getFirstName).setHeader("First name");
        usersGrid.addColumn(UserDto::getLastname).setHeader("Surname");
        usersGrid.addColumn(UserDto::getEmail).setHeader("Email");
        usersGrid.addColumn(UserDto::getUserStatus).setHeader("Status");
        usersGrid.setItems(allUsers);
        return usersGrid;
    }

    private void refreshGrid() {
        List<UserDto> users = service.getAllUsers();
        usersGrid.setItems(users);
    }

    private void openDialog(UserDto user) {
        TextField nameField = new TextField("First name");
        TextField surnameField = new TextField("Surname");
        binder.bind(nameField, UserDto::getFirstName, UserDto::setFirstName);
        binder.bind(surnameField, UserDto::getLastname, UserDto::setLastname);
        binder.readBean(user);
        Button saveButton = new Button("Save", e -> {
            if (binder.writeBeanIfValid(user)) {
                service.updateUser(user);
                refreshGrid();
            }
            dialog.close();
        });
        Button cancelButton = ButtonHelper.createDialogCancelButton(dialog);

        VerticalLayout dialogLayout = new VerticalLayout(nameField, surnameField, saveButton, cancelButton);
        dialog.removeAll();
        dialog.add(dialogLayout);
        dialog.open();
        dialogLayout.setBoxSizing(BoxSizing.CONTENT_BOX);
    }
}
