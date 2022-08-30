package com.petrovskiy.epm.service.impl;

import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.model.User;
import com.petrovskiy.epm.service.UserService;
import com.petrovskiy.epm.service.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private ModelMapper map = new ModelMapper();

    @Override
    public UserDto create(UserDto userDto) {
        User user = map.map(userDto,User.class);
        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> user  = userRepository.findById(id); //orElseGet
        return map.map(user.get(),UserDto.class);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
