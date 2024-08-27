package com.app.rotatio.vaadin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkerListDto {
    private String names;
    private String taskName;
    private String workplaceDesignation;
}
