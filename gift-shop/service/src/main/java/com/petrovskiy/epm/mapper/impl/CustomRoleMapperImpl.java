package com.petrovskiy.epm.mapper.impl;

import com.petrovskiy.epm.dto.RoleDto;
import com.petrovskiy.epm.mapper.PrivilegeMapper;
import com.petrovskiy.epm.mapper.RoleMapper;
import com.petrovskiy.epm.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomRoleMapperImpl implements RoleMapper {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    public Role dtoToRole(RoleDto roleDto) {
        if (roleDto.getPrivilege() != null) {
            return Role.builder()
                    .id(roleDto.getId())
                    .name(roleDto.getName())
                    .privilege(roleDto.getPrivilege()
                            .stream()
                            .map(privilegeMapper::dtoToPrivilege)
                            .collect(Collectors.toSet())).build();
        } else {
            return Role.builder()
                    .id(roleDto.getId())
                    .name(roleDto.getName()).build();
        }
    }

    public RoleDto roleToDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .privilege(role.getPrivilege()
                        .stream()
                        .map(privilegeMapper::privilegeToDto)
                        .collect(Collectors.toSet())).build();
    }
}
