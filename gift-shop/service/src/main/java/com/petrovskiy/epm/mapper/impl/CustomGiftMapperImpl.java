package com.petrovskiy.epm.mapper.impl;

import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.mapper.GiftCertificateMapper;
import com.petrovskiy.epm.mapper.TagMapper;
import com.petrovskiy.epm.mapper.TagMapperImpl;
import com.petrovskiy.epm.model.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomGiftMapperImpl implements GiftCertificateMapper {

    private final TagMapper tagMapper = new TagMapperImpl();

    @Override
    public GiftCertificate dtoToGift(GiftCertificateDto giftCertificateDto) {
        return GiftCertificate.builder()
                .id(giftCertificateDto.getId())
                .name(giftCertificateDto.getName())
                .price(giftCertificateDto.getPrice())
                .duration(giftCertificateDto.getDuration())
                .createDate(giftCertificateDto.getCreateDate())
                .lastUpdateDate(giftCertificateDto.getLastUpdateDate())
                .description(giftCertificateDto.getDescription())
                .tagList(giftCertificateDto.getTagDtoList().stream().map(tagMapper::dtoToTag).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public GiftCertificateDto giftToDto(GiftCertificate giftCertificate) {
        return GiftCertificateDto.builder()
                .id(giftCertificate.getId())
                .name(giftCertificate.getName())
                .price(giftCertificate.getPrice())
                .duration(giftCertificate.getDuration())
                .createDate(giftCertificate.getCreateDate())
                .lastUpdateDate(giftCertificate.getLastUpdateDate())
                .description(giftCertificate.getDescription())
                .tagDtoList(giftCertificate.getTagList().stream().map(tagMapper::tagToDto).toList())
                .build();
    }
}
