package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.app.rotatio.vaadin.domain.dto.WorkplaceDto;
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

    public WorkerViewService(RestTemplate restTemplate, PlanViewService planViewService) {
        super(restTemplate);
        this.planViewService = planViewService;
    }

    public List<WorkerDto> getAllWorkers() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    public List<WorkerDto> getWorkersByPresenceTo(LocalDate date) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers/byAbsenceFrom/" + date,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    public List<WorkerDto> getWorkersByPresenceFrom(LocalDate date) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers/byPresenceBefore/" + date,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    public List<WorkerDto> getWorkersByPlan(LocalDate value) {
        WorkingDayDto planByExecuteDay = planViewService.getPlanByExecuteDay(value);
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers/byWorkingDay/" + planByExecuteDay.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {
                }
        ).getBody();
    }

    public List<WorkerDto> getWorkersByStatus(final int value) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers/byStatus/" + value,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {}
        ).getBody();
    }

    public WorkerDto createWorker(WorkerDto worker) {
        HttpEntity<WorkerDto> request = new HttpEntity<>(worker);
        return restTemplate.postForObject(
                "http://localhost:8080/v1/rotatio/workers",
                request,
                WorkerDto.class
        );
    }

    public void updateWorker(WorkerDto worker) {
        HttpEntity<WorkerDto> request = new HttpEntity<>(worker);
        ResponseEntity<WorkerDto> response = restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers",
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
                "http://localhost:8080/v1/rotatio/workers/" + id,
                HttpMethod.DELETE,
                null,
                WorkerDto.class
        ).getBody();

    }
}
