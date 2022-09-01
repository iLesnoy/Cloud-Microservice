package com.petrovskiy.epm.converter;

import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDto userToDto(User user){
        return null;/*UserDto.builder().id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole()).build();*/
    }
}
