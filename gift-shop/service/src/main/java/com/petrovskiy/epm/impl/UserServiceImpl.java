package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.converter.UserConverter;
import com.petrovskiy.epm.dao.RoleRepository;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.model.Role;
import com.petrovskiy.epm.model.User;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    /*private PasswordEncoder passwordEncoder;*/

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userConverter = userConverter;
    }

    private final ModelMapper mapper = new ModelMapper();

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        userRepository.findAll();
        User user = mapper.map(userDto,User.class);
        setParamsToNewUser(user);
        userRepository.save(user);
        return userDto;
    }

    private void setParamsToNewUser(User user){
        Role role = roleRepository.findByName("USER");
        user.setRoles(Set.of(role));
    }



    @Override
    public UserDto update(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> user  = userRepository.findById(id); //orElseGet
        return userConverter.userToDto(user.get());
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> orderPage = userRepository.findAll(pageable);
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(userConverter::userToDto);
    }

    @Override
    public void delete(Long id) {

    }
}
