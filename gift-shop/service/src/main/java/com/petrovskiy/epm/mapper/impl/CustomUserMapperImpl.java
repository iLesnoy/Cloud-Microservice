package com.petrovskiy.epm.mapper.impl;

import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.model.User;

import java.util.stream.Collectors;

public class CustomUserMapperImpl {

    /*public UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().stream().collect(Collectors.toSet()))
                .build();
    }*/
}
