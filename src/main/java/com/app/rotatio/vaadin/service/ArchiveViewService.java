package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.ArchiveDto;
import com.app.rotatio.vaadin.domain.dto.TaskDto;
import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ArchiveViewService extends BaseViewService {
    private  final WorkerViewService workerViewService;
    public ArchiveViewService(RestTemplate restTemplate, WorkerViewService workerViewService) {
        super(restTemplate);
        this.workerViewService = workerViewService;
    }


    public List<ArchiveDto> getAllArchives() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/archives",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ArchiveDto>>() {}
        ).getBody();
    }

    public ArchiveDto getByPlan(Long id) {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/archives/byWorkingDay/" + id,
                ArchiveDto.class
        );
    }

    public void deleteArchive(Long id) {
        restTemplate.delete("http://localhost:8080/v1/rotatio/archives/delete/" + id);
    }

    public void printArchive(ArchiveDto archiveDto) {
    }

    public List<WorkerDto> getPlannedWorkers(Long workingDayId) {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workers/byWorkingDay/" + workingDayId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {}
        ).getBody();
    }
}
