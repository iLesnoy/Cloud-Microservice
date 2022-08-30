package com.petrovskiy.epm.service.dto;

import com.petrovskiy.epm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto user){
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping
    public Page<UserDto> findAllUsers(@PathVariable Pageable pageable){
        return userService.findAll(pageable);
    }
}
