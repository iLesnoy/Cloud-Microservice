package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@Builder
@JsonPropertyOrder({"id","name","privileges"})
public class RoleDto extends RepresentationModel<RoleDto> {

    private Long id;

    private String name;

    private Set<PrivilegeDto> privilege;
}
