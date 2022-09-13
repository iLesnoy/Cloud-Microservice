package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.model.GiftCertificateAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GiftCertificateAttributeMapper {
    GiftCertificateAttribute convert(GiftCertificateAttributeDto source);
}
