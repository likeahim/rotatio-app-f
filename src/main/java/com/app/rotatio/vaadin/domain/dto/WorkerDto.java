package com.app.rotatio.vaadin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkerDto {
        private Long id;
        private Long workerNumber;
        private String firstName;
        private String lastname;
        private int status;
        private Long workingDayId;
        private Long taskId;
        private Long workplaceId;
}
