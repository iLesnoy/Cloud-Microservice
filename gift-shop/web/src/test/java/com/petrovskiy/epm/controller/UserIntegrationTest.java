package com.petrovskiy.epm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.model.Order;
import com.petrovskiy.epm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;

    private User expected;
    private List<User> userList;
    private List<Order> orderList;
    private Order order;


    @BeforeEach
    public void initMethod(){
        order = Order.builder().id(1L).orderDate(LocalDateTime.now()).build();
        orderList = List.of(order);
        expected = new User();
        expected.setFirstName("yarik");
        expected.setLastName("egorik");
        expected.setEmail("yafgdg20@tut.by");
        expected.setId(1L);
        expected.getOrderList();
        userList = List.of(expected);
    }

    @Test
    void findAll() throws Exception {
        userRepository.save(expected);
        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(userList))).andExpect(status().isOk());
        List<User> user = userRepository.findAll();
        assertTrue(user.size() > 0);
    }


    @Test
    void findById() throws Exception {
        userRepository.save(expected);
        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(expected))).andExpect(status().isOk());
        Optional<User> user = userRepository.findById(1L);
        assertTrue(user.isPresent());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(expected))).andExpect(status().isCreated());
        User actual = userRepository.save(expected);
        assertEquals(actual.getEmail(), expected.getEmail());

    }

    @Test
    void deleteUser() throws Exception {
        userRepository.save(expected);
        mockMvc.perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(expected))).andExpect(status().isOk());
        userRepository.delete(expected);
        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    void updateUser() throws Exception {
        userRepository.save(expected);
        mockMvc.perform(patch("/api/users/1").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(expected))).andExpect(status().isOk());
        expected.setEmail("egorik@gmail.com");
        User actual = userRepository.save(expected);
        assertEquals(actual.getEmail(), expected.getEmail());
    }

    /*@Test
    void findUserOrders() throws Exception {
        userRepository.save(expected);
        mockMvc.perform(get("/api/users//1/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(expected))).andExpect(status().isOk());
        Optional<User> user = userRepository.findUs("yafgdg20@tut.by");
        assertTrue(user.isPresent());

    }*/

    @Test
    void findByEmail() throws Exception {
        userRepository.save(expected);
        mockMvc.perform(get("/api/users/yafgdg20@tut.by").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(expected))).andExpect(status().isOk());
        Optional<User> user = userRepository.findByEmail("yafgdg20@tut.by");
        assertTrue(user.isPresent());
    }
}