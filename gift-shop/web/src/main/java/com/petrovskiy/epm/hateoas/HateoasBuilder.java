package com.petrovskiy.epm.hateoas;

import com.petrovskiy.epm.controller.GiftCertificateController;
import com.petrovskiy.epm.controller.OrderController;
import com.petrovskiy.epm.controller.TagController;
import com.petrovskiy.epm.controller.UserController;
import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasBuilder {

    private static final String SELF = "self";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String SEARCH = "search";

    private static final String TAGS = "tags";
    private static final String CERTIFICATES = "certificates";
    private static final String ORDERS = "orders";
    private static final String USERS = "users";
    private static final int DEFAULT_PAGE_SIZE = 2;

    private static final Pageable DEFAULT_PAGEABLE = Pageable.ofSize(DEFAULT_PAGE_SIZE);

    public void setLinks(TagDto tagDto) {
        setCommonLinks(TagController.class, tagDto, tagDto.getId(), TAGS, SELF, DELETE);
    }

    public void setLinks(GiftCertificateDto certificateDto) {
        setCommonLinks(GiftCertificateController.class, certificateDto, certificateDto.getId(), CERTIFICATES, SELF, UPDATE, DELETE);
        certificateDto.getTagDtoList().forEach(this::setLinks);
    }

    public void setLinks(ResponseOrderDto orderDto) {
        setCommonLinks(OrderController.class, orderDto, orderDto.getId(), ORDERS, SELF);
        orderDto.getCertificateList().forEach(this::setLinks);
        setLinks(orderDto.getUserDto());
    }

    public void setLinks(UserDto userDto) {
        setCommonLinks(UserController.class, userDto, userDto.getId(), USERS, SELF, DELETE);
        userDto.add(linkTo(methodOn(UserController.class).findByName(userDto.getEmail())).withRel(SEARCH));
        userDto.add(linkTo(methodOn(UserController.class).findUserOrders(userDto.getId(), DEFAULT_PAGEABLE)).withRel(ORDERS));
    }

    private <T extends RepresentationModel<T>> void setCommonLinks(Class<?> controllerClass, T entity, Long id
            , String LinkNameForPlural, String... linkNames) {
        Arrays.stream(linkNames).forEach(linkName -> entity.add(linkTo(controllerClass).slash(id).withRel(linkName)));
        entity.add(linkTo(controllerClass).withRel(LinkNameForPlural));
    }
}
