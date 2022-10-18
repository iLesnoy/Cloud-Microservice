package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.hateoas.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private HateoasBuilder hateoasBuilder;


    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@RequestBody @Valid UserDto userDto){
        hateoasBuilder.setLinks(userDto);
        return  userService.create(userDto);
    }

    @PostMapping("")
    public UserDto login(@RequestBody UserDto userDto){
        return userService.findByName(userDto.getFirstName());
    }

}
