package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.PrivilegeDto;
import com.petrovskiy.epm.model.Privilege;
import org.mapstruct.Mapper;

@Mapper
public interface PrivilegeMapper {

    Privilege dtoToPrivilege(PrivilegeDto privilegeDto);

    PrivilegeDto privilegeToDto(Privilege privilege);

}
