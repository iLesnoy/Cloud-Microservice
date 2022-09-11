package com.petrovskiy.epm.mapper.impl;

import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.mapper.OrderMapper;
import com.petrovskiy.epm.mapper.UserMapper;
import com.petrovskiy.epm.mapper.UserMapperImpl;
import com.petrovskiy.epm.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomOrderMapperImpl implements OrderMapper {

    private UserMapper userMapper = new UserMapperImpl();
    private CustomGiftMapperImpl customGiftMapper;

    @Autowired
    public CustomOrderMapperImpl(CustomGiftMapperImpl customGiftMapper) {
        this.customGiftMapper = customGiftMapper;
    }

    @Override
    public Order dtoToOrder(ResponseOrderDto order) {
        return Order.builder()
                .id(order.getId())
                .purchaseTime(order.getOrderDate())
                .cost(order.getCost())
                .user(userMapper.dtoToUser(order.getUserDto()))
                .certificateList(order.getCertificateList().stream().map(customGiftMapper::dtoToGift).toList())
                .build();
    }

    @Override
    public ResponseOrderDto orderToDto(Order order) {
        return ResponseOrderDto.builder()
                .id(order.getId())
                .orderDate(order.getPurchaseTime())
                .cost(order.getCost())
                .userDto(userMapper.userToDto(order.getUser()))
                .certificateList(order.getCertificateList().stream().map(customGiftMapper::giftToDto).toList())
                .build();
    }

}
