package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User dtoToUser (UserDto userDto);
    UserDto userToDto(User user);

}
