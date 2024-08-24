package com.app.rotatio.vaadin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class WorkerDto {
        private Long id;
        private Long workerNumber;
        private String firstName;
        private String lastname;
        private int status;
        private LocalDate presenceFrom;
        private LocalDate absenceFrom;
        private Long workingDayId;
        private Long taskId;
        private Long workplaceId;
}
