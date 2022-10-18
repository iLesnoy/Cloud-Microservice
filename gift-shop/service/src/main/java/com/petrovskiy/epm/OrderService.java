package com.petrovskiy.epm;

import com.petrovskiy.epm.dto.RequestOrderDto;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService extends BaseService<ResponseOrderDto>{

    ResponseOrderDto create(RequestOrderDto order);

}
