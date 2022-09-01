package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.RoleDto;
import com.petrovskiy.epm.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    Role dtoToRole(RoleDto roleDto);
    RoleDto roleToDto(Role role);
}
