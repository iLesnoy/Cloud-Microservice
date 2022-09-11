package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.UserService;
import com.petrovskiy.epm.dao.OrderRepository;
import com.petrovskiy.epm.dao.RoleRepository;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.UserMapper;
import com.petrovskiy.epm.mapper.UserMapperImpl;
import com.petrovskiy.epm.mapper.impl.CustomOrderMapperImpl;
import com.petrovskiy.epm.model.Order;
import com.petrovskiy.epm.model.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.petrovskiy.epm.exception.ExceptionCode.DUPLICATE_NAME;
import static com.petrovskiy.epm.exception.ExceptionCode.NON_EXISTENT_ENTITY;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper = new UserMapperImpl();
    private final OrderRepository orderRepository;
    private final CustomOrderMapperImpl orderMapper;
    /*private PasswordEncoder passwordEncoder;*/

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           OrderRepository orderRepository,CustomOrderMapperImpl orderMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;

    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).ifPresent(a -> {
            throw new SystemException(DUPLICATE_NAME);
        });
        setParamsToNewUser(userDto);
        User user = userMapper.dtoToUser(userDto);
        return userMapper.userToDto(userRepository.save(user));
    }

    private void setParamsToNewUser(UserDto user){
        roleRepository.findByName("USER").ifPresent(a->{
            user.setRole(Set.of(a));
        });
    }


    @Override
    public UserDto update(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new com.petrovskiy.epm.exception.SystemException(NON_EXISTENT_ENTITY));
        return userMapper.userToDto(user);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> orderPage = userRepository.findAll(pageable);
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(userMapper::userToDto);
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new SystemException(NON_EXISTENT_ENTITY));
        userRepository.delete(user);
    }

    @SneakyThrows
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @SneakyThrows
    @Override
    public UserDto findByName(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new SystemException(NON_EXISTENT_ENTITY));
        return userMapper.userToDto(user);
    }


    @SneakyThrows
    @Override
    public Page<ResponseOrderDto> findUserOrderList(Long id, Pageable pageable) {
        if (!userRepository.existsById(id)) {
            throw new SystemException(NON_EXISTENT_ENTITY);
        }
        Page<Order> orderPage = orderRepository.findOrderByUserId(id, pageable);
        /*if (!validation.isPageExists(pageable, orderPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }*/
        return new CustomPage<>(orderPage.getContent(), orderPage.getPageable(), orderPage.getTotalElements())
                .map(orderMapper::orderToDto);
    }



}
