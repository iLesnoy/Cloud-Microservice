package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dao.OrderRepository;
import com.petrovskiy.epm.dao.RoleRepository;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.OrderMapper;
import com.petrovskiy.epm.mapper.UserMapper;
import com.petrovskiy.epm.model.Order;
import com.petrovskiy.epm.model.Role;
import com.petrovskiy.epm.model.User;
import com.petrovskiy.epm.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.petrovskiy.epm.exception.ExceptionCode.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final EntityValidator validator;
    /*private PasswordEncoder passwordEncoder;*/

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           OrderRepository orderRepository, OrderMapper orderMapper,
                           EntityValidator validator, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.validator = validator;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).ifPresentOrElse(a -> {
                    throw new SystemException(DUPLICATE_NAME);
                }, () -> setRoleToNewUser(userDto)
        );
        User user = userMapper.dtoToUser(userDto);
        return userMapper.userToDto(userRepository.save(user));
    }

    private void setRoleToNewUser(UserDto user) {
        roleRepository.findByName("USER").ifPresent(a ->
                user.setRole(Set.of(a)));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        AtomicReference<User> persistedUser = new AtomicReference<>();
        userRepository.findById(id).ifPresentOrElse(user -> {
            setUpdatedData(user,userDto);
            persistedUser.set(userRepository.save(user));
        }, () -> {
            throw new SystemException(NON_EXISTENT_ENTITY);
        });
        return userMapper.userToDto(persistedUser.get());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
        return userMapper.userToDto(user);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> orderPage = userRepository.findAll(pageable);
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(userMapper::userToDto);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
        userRepository.delete(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    public UserDto findByName(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
        return userMapper.userToDto(user);
    }


    @Override
    public Page<ResponseOrderDto> findUserOrderList(Long id, Pageable pageable) {
        if (!userRepository.existsById(id)) {
            throw new SystemException(NON_EXISTENT_ENTITY);
        }
        Page<Order> orderPage = orderRepository.findOrderByUserId(id, pageable);
        if (!validator.isPageExists(pageable, orderPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(orderMapper::orderToDto);
    }

    private void setUpdatedData(User user, UserDto userDto) {
        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Set<Role> roles = userDto.getRole();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(roles);
    }
}
