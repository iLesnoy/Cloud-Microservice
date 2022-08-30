package com.petrovskiy.epm.service;

import com.petrovskiy.epm.service.dto.RequestOrderDto;
import com.petrovskiy.epm.service.dto.ResponseOrderDto;

public interface OrderService extends BaseService<ResponseOrderDto>{

    ResponseOrderDto create(RequestOrderDto order);
}
