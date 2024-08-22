package com.app.rotatio.vaadin.domain.dto;

import java.time.LocalDate;

public record WorkerDto(
        Long id,
        Long workerNumber,
        String firstName,
        String lastname,
        int status,
        LocalDate presenceFrom,
        LocalDate absenceFrom,
        Long workingDayId,
        Long taskId,
        Long workplaceId
) {
}
