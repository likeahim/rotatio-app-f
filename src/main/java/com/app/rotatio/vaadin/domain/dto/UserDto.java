package com.app.rotatio.vaadin.domain.dto;

import java.util.List;

public record UserDto (
        Long id,
        String firstName,
        String lastname,
        String email,
        String password,
        String userStatus,
        String objectId,
        String userToken,
        List<Long> plannedDays
){
}
