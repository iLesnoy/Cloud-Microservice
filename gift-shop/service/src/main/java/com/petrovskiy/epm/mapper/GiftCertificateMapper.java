package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.model.GiftCertificate;
import org.mapstruct.Mapper;

@Mapper
public interface GiftCertificateMapper {
    GiftCertificate dtoToGift(GiftCertificateDto giftCertificateDto);
    GiftCertificateDto giftToDto(GiftCertificate giftCertificate);
}
