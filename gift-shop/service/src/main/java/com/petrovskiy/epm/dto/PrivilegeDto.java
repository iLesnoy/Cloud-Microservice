package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "name"})
public class PrivilegeDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}
