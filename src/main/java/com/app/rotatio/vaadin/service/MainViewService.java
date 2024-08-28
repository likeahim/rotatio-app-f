package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MainViewService extends BaseViewService {

    private final WorkerViewService workerViewService;

    public MainViewService(RestTemplate restTemplate, EndpointConfig endpointConfig, WorkerViewService workerViewService) {
        super(restTemplate, endpointConfig);
        this.workerViewService = workerViewService;
    }

    public int getForecastWorkersCount() {
        List<WorkerDto> presentWorkers = workerViewService.getWorkersByStatus(1);
        return presentWorkers.isEmpty() ? 0 : presentWorkers.size();
    }

    public int getEmployedWorkersCount() {
        List<WorkerDto> allWorkers = workerViewService.getAllWorkers();
        List<WorkerDto> unemployedWorkers = workerViewService.getWorkersByStatus(3);
        int allWorkersCount = allWorkers.isEmpty() ? 0 : allWorkers.size();
        int unemployedWorkersCount = unemployedWorkers.isEmpty() ? 0 : unemployedWorkers.size();
        return allWorkersCount - unemployedWorkersCount;
    }
}
