package com.app.rotatio.vaadin.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskDto {
        private Long id;
        @NotNull
        private String name;
        private String description;
        private boolean isPerformed;
}