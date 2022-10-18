package com.petrovskiy.epm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petrovskiy.epm.dao.PrivilegeRepository;
import com.petrovskiy.epm.dao.RoleRepository;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.mapper.UserMapper;
import com.petrovskiy.epm.model.Privilege;
import com.petrovskiy.epm.model.Role;
import com.petrovskiy.epm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    private UserDto expected;
    private Role role;
    private Privilege privilege;

    private MockMvc mockMvc;
    private UserMapper userMapper;
    private UserRepository userRepository;
    private ObjectMapper mapper;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;

    @Autowired
    public AuthenticationControllerTest(MockMvc mockMvc, UserMapper userMapper, UserRepository userRepository,
                                        ObjectMapper mapper, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.mockMvc = mockMvc;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @BeforeEach
    void setUp() {
        privilege = Privilege.builder().id(1L).name("update").build();
        role = Role.builder()
                .id(1L)
                .name("USER")
                .privilege(Set.of(privilege))
                .build();
        expected =  UserDto.builder()
                .id(1L)
                .email("el@gmail.com")
                .firstName("Egot")
                .lastName("petrov")
                .password("dqwew1e2131231")
                .role(Set.of(role))
                .build();

    }


    @Test
    void signUp() throws Exception {
        /*privilegeRepository.save(privilege);
        roleRepository.save(role);*/

        mockMvc.perform(post("/api/users/auth/signUp").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(expected)))
                .andExpect(status().isCreated());
        User actual = userRepository.save(userMapper.dtoToUser(expected));
        assertEquals(actual.getEmail(), expected.getEmail());
    }

    @Test
    void signUpInvalidEmail() throws Exception {
        privilegeRepository.save(privilege);
        roleRepository.save(role);

        expected.setEmail("");

        mockMvc.perform(post("/api/users/auth/signUp").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(expected)))
                .andExpect(status().isCreated());
        User actual = userRepository.save(userMapper.dtoToUser(expected));
        assertEquals(actual.getEmail(), expected.getEmail());
    }

    @Test
    void login() {
    }
}