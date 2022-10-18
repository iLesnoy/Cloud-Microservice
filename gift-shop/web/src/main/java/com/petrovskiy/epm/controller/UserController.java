package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.hateoas.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HateoasBuilder hateoasBuilder;

    @Autowired
    public UserController(UserService userService, HateoasBuilder hateoasBuilder) {
        this.userService = userService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @GetMapping
    public Page<UserDto> findAll(@PageableDefault(value = 2, page = 0) Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping("/{id}/orders")
    public Page<ResponseOrderDto> findUserOrders(@PageableDefault(value = 2, page = 0)
                                                 @PathVariable Long id, Pageable pageable) {
        Page<ResponseOrderDto> page = userService.findUserOrderList(id, pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @GetMapping("/search/{name}")
    /*@PreAuthorize("hasAuthority('users:read') || #name.equals(authentication.principal.username)")*/
    public UserDto findByName(@PathVariable String name) {
        UserDto userDto = userService.findByName(name);
        hateoasBuilder.setLinks(userDto);
        return userDto;
    }


}
