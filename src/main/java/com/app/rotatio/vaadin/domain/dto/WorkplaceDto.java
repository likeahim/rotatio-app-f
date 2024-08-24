package com.app.rotatio.vaadin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkplaceDto {
    private Long id;
    private String designation;
    private boolean active;
    private boolean nowUsed;
}
