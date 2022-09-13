package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.RoleDto;
import com.petrovskiy.epm.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    Role dtoToRole(RoleDto roleDto);
    RoleDto roleToDto(Role role);
}
