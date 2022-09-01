package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.model.GiftCertificateAttribute;
import org.mapstruct.Mapper;

@Mapper
public interface GiftCertificateAttributeMapper {
    GiftCertificateAttribute convert(GiftCertificateAttributeDto source);
}
