package com.app.rotatio.vaadin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class WorkingDayDto {
        private Long id;
        private LocalDate created;
        private LocalDate executeDate;
        private Boolean planned;
        private Boolean archived;
        private Long userId;
        private List<Long> workers;
}
