package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.dto.RequestOrderDto;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.hateoas.HateoasBuilder;
import com.petrovskiy.epm.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderServiceImpl orderService;
    private HateoasBuilder hateoasBuilder;

    @Autowired
    public OrderController(OrderServiceImpl orderService,HateoasBuilder hateoasBuilder) {
        this.orderService = orderService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @GetMapping
    public Page<ResponseOrderDto> findAll(@PageableDefault(value = 2, page = 0) Pageable pageable) {
        Page<ResponseOrderDto> page = orderService.findAll(pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @GetMapping("/{id}")
    public ResponseOrderDto findById(@PathVariable Long id) {
        hateoasBuilder.setLinks(orderService.findById(id));
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseOrderDto create(@RequestBody RequestOrderDto orderDto) {
        hateoasBuilder.setLinks(orderService.create(orderDto));
        return orderService.create(orderDto);
    }
}