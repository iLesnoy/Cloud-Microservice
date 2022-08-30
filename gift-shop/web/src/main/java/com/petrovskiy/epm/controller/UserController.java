package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.service.UserService;
import com.petrovskiy.epm.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Page<UserDto> findAll(@PathVariable Pageable pageable) {
        return userService.findAll(pageable);
    }
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

}
