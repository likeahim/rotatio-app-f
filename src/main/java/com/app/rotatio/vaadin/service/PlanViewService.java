package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.ArchiveDto;
import com.app.rotatio.vaadin.domain.dto.TaskDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanViewService extends BaseViewService {

    public PlanViewService(RestTemplate restTemplate, EndpointConfig endpointConfig) {
        super(restTemplate, endpointConfig);
    }

    public List<WorkingDayDto> getAllPlans() {
        return restTemplate.exchange(
                endpointConfig.getPlansProdEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {
                }
        ).getBody();
    }

    public List<WorkingDayDto> getUsersPlans(Long userId) {
        return restTemplate.exchange(
                endpointConfig.getPlansByUserEndpoint() + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {
                }
        ).getBody();
    }

    public List<WorkingDayDto> getPlansByPlanned(boolean planned) {
        return restTemplate.exchange(
                endpointConfig.getPlansByPlannedEndpoint() + planned,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {
                }
        ).getBody();
    }

    public List<WorkingDayDto> getPlansByArchived(boolean archived) {
        return restTemplate.exchange(
                endpointConfig.getPlansByArchivedEndpoint() + archived,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {}
        ).getBody();
    }

    public WorkingDayDto getPlanByExecuteDay(LocalDate executeDay) {
        return restTemplate.getForObject(
                endpointConfig.getPlansByExecuteEndpoint() + executeDay,
                WorkingDayDto.class
        );
    }

    public WorkingDayDto createPlan(LocalDate created, LocalDate execute, Long userId) {
        WorkingDayDto workingDayDto = new WorkingDayDto(
                null, created, execute, false, false, userId, new ArrayList<>()
        );
        HttpEntity<WorkingDayDto> request = new HttpEntity<>(workingDayDto);
        return restTemplate.postForObject(
                endpointConfig.getPlansProdEndpoint(),
                request,
                WorkingDayDto.class
        );
    }

    public void updatePlan(WorkingDayDto workingDay) {
        HttpEntity<WorkingDayDto> request = new HttpEntity<>(workingDay);
        ResponseEntity<TaskDto> response = restTemplate.exchange(
                endpointConfig.getPlansProdEndpoint(),
                HttpMethod.PUT,
                request,
                TaskDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Notification.show("Plan updated successfully!");
        } else {
            Notification.show("Failed to update plan");
        }
    }

    public ArchiveDto archivePlan(Long workingDayId) {
        return restTemplate.postForObject(
                endpointConfig.getPlansProdEndpoint() + "/" + workingDayId,
              null,
              ArchiveDto.class
        );
    }

    public WorkingDayDto getPlanById(Long workingDayId) {
        return restTemplate.getForObject(
                endpointConfig.getPlansProdEndpoint() + "/" + workingDayId,
                WorkingDayDto.class
        );
    }
}