package com.app.rotatio.vaadin.service;

import com.app.rotatio.vaadin.domain.dto.UserDto;
import com.app.rotatio.vaadin.domain.dto.WorkingDayDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanViewService extends BaseViewService{

    public PlanViewService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public List<WorkingDayDto> getWorkingDays() {
        return restTemplate.exchange(
                "http://localhost:8080/v1/rotatio/workingDays",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<WorkingDayDto>>() {}
        ).getBody();
    }

    public UserDto getUserById(Long userId) {
        return restTemplate.getForObject(
                "http://localhost:8080/v1/rotatio/users/" + userId,
                UserDto.class
        );
    }

    public WorkingDayDto createWorkingDay(LocalDate created, LocalDate execute, Long userId) {
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
}