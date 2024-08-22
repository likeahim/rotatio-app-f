package com.app.rotatio.vaadin.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegisterDto(
        @JsonProperty("fistName")
        String firstName,
        @JsonProperty("lastname")
        String lastname,
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password
) {
}
