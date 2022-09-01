package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "name"})
public class RoleDto {

    private Long id;

    private String name;
}
