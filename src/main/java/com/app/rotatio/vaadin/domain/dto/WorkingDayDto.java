package com.app.rotatio.vaadin.domain.dto;

import java.time.LocalDate;
import java.util.List;

public record WorkingDayDto(
        Long id,
        LocalDate created,
        LocalDate executeDate,
        boolean planned,
        boolean archived,
        Long userId,
        List<Long> workers
) {
}
