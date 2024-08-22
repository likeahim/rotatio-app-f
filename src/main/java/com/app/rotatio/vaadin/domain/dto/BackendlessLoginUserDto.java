package com.app.rotatio.vaadin.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackendlessLoginUserDto {
    private String login;
    private String password;
    @JsonProperty("user-token")
    private String userToken;
}
