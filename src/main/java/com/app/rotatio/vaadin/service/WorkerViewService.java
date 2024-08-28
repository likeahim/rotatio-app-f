package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkerViewService extends BaseViewService {

    private final PlanViewService planViewService;

    public WorkerViewService(RestTemplate restTemplate, EndpointConfig endpointConfig, PlanViewService planViewService) {
        super(restTemplate, endpointConfig);
        this.planViewService = planViewService;
    }

    public List<WorkerDto> getAllWorkers() {
        return restTemplate.exchange(
                endpointConfig.getWorkersProdEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    public List<WorkerDto> getWorkersByPlan(LocalDate value) {
        WorkingDayDto planByExecuteDay = planViewService.getPlanByExecuteDay(value);
        return restTemplate.exchange(
                endpointConfig.getWorkersByPlanEndpoint() + planByExecuteDay.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    public List<WorkerDto> getWorkersByStatus(final int value) {
        return restTemplate.exchange(
                endpointConfig.getWorkersByStatusEndpoint() + value,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {}
        ).getBody();
    }

    public WorkerDto createWorker(WorkerDto worker) {
        HttpEntity<WorkerDto> request = new HttpEntity<>(worker);
        return restTemplate.postForObject(
                endpointConfig.getWorkersProdEndpoint(),
                request,
                WorkerDto.class
        );
    }

    public void updateWorker(WorkerDto worker) {
        HttpEntity<WorkerDto> request = new HttpEntity<>(worker);
        ResponseEntity<WorkerDto> response = restTemplate.exchange(
                endpointConfig.getWorkersProdEndpoint(),
                HttpMethod.PUT,
                request,
                WorkerDto.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Notification.show("Worker updated successfully.");
        } else {
            Notification.show("Failed to update worker.");
        }
    }

    public WorkerDto deleteWorker(Long id) {
        return restTemplate.exchange(
                endpointConfig.getWorkersProdEndpoint() + "/" + id,
                HttpMethod.DELETE,
                null,
                WorkerDto.class
        ).getBody();

    }
}
