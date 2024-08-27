package com.app.rotatio.vaadin.mapper;

import com.app.rotatio.vaadin.domain.dto.WorkerDto;
import com.app.rotatio.vaadin.domain.dto.WorkerListDto;
import org.springframework.stereotype.Service;

@Service
public class WorkerMapper {
    public WorkerListDto mapToWorkerListDto(WorkerDto workerDto, String taskName, String workplaceDesignation) {
        return new WorkerListDto(
                workerDto.getFirstName() + " " + workerDto.getLastname(),
                taskName,
                workplaceDesignation
        );
    }
}
