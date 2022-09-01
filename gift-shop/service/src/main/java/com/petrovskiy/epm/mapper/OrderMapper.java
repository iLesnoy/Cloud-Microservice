package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.model.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    Order dtoToOrder(ResponseOrderDto order);
    ResponseOrderDto orderToDto(Order order);
}
