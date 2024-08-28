package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.WorkplaceDto;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WorkplaceViewService extends BaseViewService {

    public WorkplaceViewService(RestTemplate restTemplate, EndpointConfig endpointConfig) {
        super(restTemplate, endpointConfig);
    }

    public List<WorkplaceDto> getAllWorkplaces() {
        return restTemplate.exchange(
                endpointConfig.getWorkplacesProdEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkplaceDto>>() {}
        ).getBody();
    }

    public WorkplaceDto createWorkplace(String designation, boolean isActive, boolean used) {
        WorkplaceDto workplaceDto = new WorkplaceDto(null, designation, isActive, used);
        HttpEntity<WorkplaceDto> request = new HttpEntity<>(workplaceDto);
        return restTemplate.postForObject(
                endpointConfig.getWorkplacesProdEndpoint(),
                request,
                WorkplaceDto.class
        );
    }

    public void updateWorkplace(WorkplaceDto workplaceDto) {
        HttpEntity<WorkplaceDto> request = new HttpEntity<>(workplaceDto);
        ResponseEntity<WorkplaceDto> response = restTemplate.exchange(
                endpointConfig.getWorkplacesProdEndpoint(),
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
                endpointConfig.getWorkplacesByActiveEndpoint() + active,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkplaceDto>>() {}
        ).getBody();
    }

    public List<WorkplaceDto> getWorkplacesByUsed(boolean used) {
        return restTemplate.exchange(
                endpointConfig.getWorkplacesByNowUsedEndpoint() + used,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkplaceDto>>() {}
        ).getBody();
    }

    public WorkplaceDto getWorkplaceById(final Long id) {
        return restTemplate.getForObject(
                endpointConfig.getWorkplacesProdEndpoint() + "/" + id,
                WorkplaceDto.class
        );
    }
}

