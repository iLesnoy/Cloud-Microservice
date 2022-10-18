package com.petrovskiy.epm.service;

import com.petrovskiy.epm.BaseService;
import com.petrovskiy.epm.dto.RequestOrderDto;
import com.petrovskiy.epm.dto.ResponseOrderDto;

public interface OrderService extends BaseService<ResponseOrderDto> {

    ResponseOrderDto create(RequestOrderDto order);

}
