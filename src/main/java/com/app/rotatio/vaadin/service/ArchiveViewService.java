package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.config.EndpointConfig;
import com.app.rotatio.vaadin.domain.dto.ArchiveDto;
import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ArchiveViewService extends BaseViewService {

    public ArchiveViewService(RestTemplate restTemplate, EndpointConfig endpointConfig) {
        super(restTemplate, endpointConfig);
    }


    public List<ArchiveDto> getAllArchives() {
        return restTemplate.exchange(
                endpointConfig.getArchiveProdEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ArchiveDto>>() {}
        ).getBody();
    }

    public ArchiveDto getByPlan(Long id) {
        return restTemplate.getForObject(
                endpointConfig.getArchiveByPlanEndpoint() + id,
                ArchiveDto.class
        );
    }

    public void deleteArchive(Long id) {
        restTemplate.delete(endpointConfig.getArchiveDeleteEndpoint() + id);
    }

    public List<WorkerDto> getPlannedWorkers(Long workingDayId) {
        return restTemplate.exchange(
                endpointConfig.getWorkersByPlanEndpoint() + workingDayId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkerDto>>() {}
        ).getBody();
    }
}
