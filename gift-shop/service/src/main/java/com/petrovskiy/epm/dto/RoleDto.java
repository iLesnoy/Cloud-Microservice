package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@JsonPropertyOrder({"id","name","privileges"})
public class RoleDto {

    private Long id;

    private String name;

    private Set<PrivilegeDto> privilege;
}
