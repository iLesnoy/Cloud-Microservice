package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.petrovskiy.epm.model.Role;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@JsonPropertyOrder({"id", "name", "role"})
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty(access = WRITE_ONLY)
    private String password;

    @JsonProperty("role")
    private List<Role> role;
}
