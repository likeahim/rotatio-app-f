package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.service.BaseViewService;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

@Route("user-data")
public class UserData extends BaseView {

    public UserData(BaseViewService baseViewService, RestTemplate restTemplate) {
        super(baseViewService, restTemplate);
    }
}
