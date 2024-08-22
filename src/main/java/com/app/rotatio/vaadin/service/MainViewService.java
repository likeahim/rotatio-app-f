package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MainViewService extends BaseViewService {

    public MainViewService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public int getForecastWorkersCount() {
        String workersUrl = "http://localhost:8080/v1/rotatio/workers";
        String workersByStatusUrl = "http://localhost:8080/v1/rotatio/workers/byStatus/3";

        WorkerDto[] allWorkers = restTemplate.getForObject(workersUrl, WorkerDto[].class);
        WorkerDto[] workersByStatus = restTemplate.getForObject(workersByStatusUrl, WorkerDto[].class);
        return (allWorkers != null ? allWorkers.length : 0) - (workersByStatus != null ? workersByStatus.length : 0);

    }

    public int getEmployedWorkersCount() {
        String forecastUrl = "http://localhost:8080/v1/rotatio/workers/byStatus/1";
        WorkerDto[] forecastWorkers = restTemplate.getForObject(forecastUrl, WorkerDto[].class);
        return forecastWorkers != null ? forecastWorkers.length : 0;
    }
}
