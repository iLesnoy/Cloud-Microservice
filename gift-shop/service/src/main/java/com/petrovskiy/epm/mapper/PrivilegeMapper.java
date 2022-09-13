package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.PrivilegeDto;
import com.petrovskiy.epm.model.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrivilegeMapper {

    Privilege dtoToPrivilege(PrivilegeDto privilegeDto);

    PrivilegeDto privilegeToDto(Privilege privilege);

}
