package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.TaskDto;
import com.app.rotatio.vaadin.domain.dto.WorkplaceDto;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WorkplaceViewService extends BaseViewService {
    public WorkplaceViewService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public List<WorkplaceDto> getAllWorkplaces() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workplaces",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkplaceDto>>() {}
        ).getBody();
    }

    public WorkplaceDto createWorkplace(String designation, boolean isActive, boolean used) {
        WorkplaceDto workplaceDto = new WorkplaceDto(null, designation, isActive, used);
        HttpEntity<WorkplaceDto> request = new HttpEntity<>(workplaceDto);
        return restTemplate.postForObject(
                "http://localhost:8080/v1/rotatio/workplaces",
                request,
                WorkplaceDto.class
        );
    }

    public void updateWorkplace(WorkplaceDto workplaceDto) {
        HttpEntity<WorkplaceDto> request = new HttpEntity<>(workplaceDto);
        ResponseEntity<WorkplaceDto> response = restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workplaces",
                HttpMethod.PUT,
                request,
                WorkplaceDto.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Notification.show("Workplace updated successfully!");
        } else {
            Notification.show("Failed to update workplace");
        }
    }

    public List<WorkplaceDto> getWorkplacesByActive(boolean active) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workplaces/byActive/" + active,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkplaceDto>>() {}
        ).getBody();
    }

    public List<WorkplaceDto> getWorkplacesByUsed(boolean used) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workplaces/byNowUsed/" + used,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkplaceDto>>() {}
        ).getBody();
    }

    public WorkplaceDto getWorkplaceById(final Long id) {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/workplaces/" + id,
                WorkplaceDto.class
        );
    }
}

