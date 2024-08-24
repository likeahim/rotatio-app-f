package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.ArchiveDto;
import com.app.rotatio.vaadin.domain.dto.TaskDto;
import com.app.rotatio.vaadin.domain.dto.UserDto;
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

    public PlanViewService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public List<WorkingDayDto> getAllPlans() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workingDays",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {
                }
        ).getBody();
    }

    public List<WorkingDayDto> getUsersPlans(Long userId) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workingDays/byUser/" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {
                }
        ).getBody();
    }

    public List<WorkingDayDto> getPlannedPlans() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workingDays/planned/" + true,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {
                }
        ).getBody();
    }

    public WorkingDayDto getPlanByExecuteDay(LocalDate executeDay) {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/workingDays/execute/" + executeDay,
                WorkingDayDto.class
        );
    }

    public WorkingDayDto createPlan(LocalDate created, LocalDate execute, Long userId) {
        WorkingDayDto workingDayDto = new WorkingDayDto(
                null, created, execute, false, false, userId, new ArrayList<>()
        );
        HttpEntity<WorkingDayDto> request = new HttpEntity<>(workingDayDto);
        return restTemplate.postForObject(
                "http://localhost:8080/v1/rotatio/workingDays",
                request,
                WorkingDayDto.class
        );
    }

    public void updatePlan(WorkingDayDto workingDay) {
        HttpEntity<WorkingDayDto> request = new HttpEntity<>(workingDay);
        ResponseEntity<TaskDto> response = restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workingDays",
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
              "http://localhost:8080/v1/rotatio/archives/" + workingDayId,
              null,
              ArchiveDto.class
        );
    }

    public WorkingDayDto getPlanById(Long workingDayId) {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/workingDays/" + workingDayId,
                WorkingDayDto.class
        );
    }
}