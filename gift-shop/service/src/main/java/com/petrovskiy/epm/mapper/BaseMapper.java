package com.petrovskiy.epm.mapper;

import org.springframework.hateoas.RepresentationModel;

public interface BaseMapper <T,B extends RepresentationModel<B>>{

    B entityToDto(T entity);
    T dtoToEntity(B dto);
}
