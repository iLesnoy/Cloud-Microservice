package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.model.GiftCertificate;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = TagMapper.class,injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GiftCertificateMapper {
    @Mapping(target = "tagList",source = "tagDtoList")
    GiftCertificate dtoToGift(GiftCertificateDto giftCertificateDto);
    @Mapping(target = "tagDtoList",source = "tagList")
    GiftCertificateDto giftToDto(GiftCertificate giftCertificate);
}
