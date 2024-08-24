package com.app.rotatio.vaadin.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
        private Long id;
        private String firstName;
        private String lastname;
        private String email;
        private String password;
        private String userStatus;
        private String objectId;
        @JsonProperty("user-token")
        private String userToken;
        private List<Long> plannedDays;
}
