package com.app.rotatio.vaadin.view;

import com.app.rotatio.vaadin.service.BaseViewService;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

@Route("workplaces")
public class WorkplaceView extends BaseView {
    public WorkplaceView(BaseViewService baseViewService, RestTemplate restTemplate) {
        super(baseViewService, restTemplate);
    }
}
