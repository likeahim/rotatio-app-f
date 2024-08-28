package com.app.rotatio.vaadin.service;


import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.TaskDto;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TaskViewService extends BaseViewService {

    public TaskViewService(RestTemplate restTemplate, EndpointConfig endpointConfig) {
        super(restTemplate, endpointConfig);
    }

    public List<TaskDto> getAllTasks() {
        return restTemplate.exchange(
                endpointConfig.getTasksProdEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TaskDto>>() {
                }
        ).getBody();
    }

    public TaskDto getTaskById(final Long id) {
        return restTemplate.getForObject(
                endpointConfig.getTasksProdEndpoint() + "/" + id,
                TaskDto.class
        );
    }

    public TaskDto createTask(final String name, final String description, final boolean isPerformed) {
        TaskDto taskDto = new TaskDto(null, name, description, isPerformed);
        HttpEntity<TaskDto> request = new HttpEntity<>(taskDto);
        return restTemplate.postForObject(
                endpointConfig.getTasksProdEndpoint(),
                request,
                TaskDto.class
        );
    }

    public void updateTask(final TaskDto task) {
        HttpEntity<TaskDto> request = new HttpEntity<>(task);
        ResponseEntity<TaskDto> response = restTemplate.exchange(
                endpointConfig.getTasksUpdateEndpoint(),
                HttpMethod.PUT,
                request,
                TaskDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Notification.show("Task updated successfully!");
        } else {
            Notification.show("Failed to update task");
        }
    }

    public List<TaskDto> getTasksByPerformed(boolean isPerformed) {
        return restTemplate.exchange(
                endpointConfig.getTasksByPerformedEndpoint() + isPerformed,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TaskDto>>() {
                }
        ).getBody();
    }
}
