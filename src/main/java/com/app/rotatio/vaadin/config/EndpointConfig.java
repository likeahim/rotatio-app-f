package com.app.rotatio.vaadin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EndpointConfig {

    @Value("${archives.endpoint.prod}")
    private String archiveProdEndpoint;
    @Value("${archives.by.plan}")
    private String archiveByPlanEndpoint;
    @Value("${archives.delete}")
    private String archiveDeleteEndpoint;
    @Value("${workers.endpoint.prod}")
    private String workersProdEndpoint;
    @Value("${workers.by.plan}")
    private String workersByPlanEndpoint;
    @Value("${workers.by.status}")
    private String workersByStatusEndpoint;
    @Value("${users.system.endpoint}")
    private String usersProdEndpoint;
    @Value("${users.system.login}")
    private String usersLoginEndpoint;
    @Value("${users.system.logout}")
    private String usersLogoutEndpoint;
    @Value("${users.system.by.email}")
    private String usersByEmailEndpoint;
    @Value("${users.syste.restore.password}")
    private String usersRestorePasswordEndpoint;
    @Value("${time.api.current}")
    private String timeApiCurrentEndpoint;
    @Value("${pdf.co.get.pdf}")
    private String pdfCoEndpoint;
    @Value("${plans.endpoint.prod}")
    private String plansProdEndpoint;
    @Value("${plans.by.user}")
    private String plansByUserEndpoint;
    @Value("${plans.by.planned}")
    private String plansByPlannedEndpoint;
    @Value("${plans.by.archived}")
    private String plansByArchivedEndpoint;
    @Value("${plans.by.execute}")
    private String plansByExecuteEndpoint;
    @Value("${tasks.endpoint.prod}")
    private String tasksProdEndpoint;
    @Value("${tasks.update}")
    private String tasksUpdateEndpoint;
    @Value("${tasks.by.performed}")
    private String tasksByPerformedEndpoint;
    @Value("${workplaces.endpoint.prod}")
    private String workplacesProdEndpoint;
    @Value("${workplaces.by.active}")
    private String workplacesByActiveEndpoint;
    @Value("${workplaces.by.now.used}")
    private String workplacesByNowUsedEndpoint;
}
