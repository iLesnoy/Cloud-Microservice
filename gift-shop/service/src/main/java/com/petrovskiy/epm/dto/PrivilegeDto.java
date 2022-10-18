package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@JsonPropertyOrder({"id", "name"})
public class PrivilegeDto extends RepresentationModel<PrivilegeDto> {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}
