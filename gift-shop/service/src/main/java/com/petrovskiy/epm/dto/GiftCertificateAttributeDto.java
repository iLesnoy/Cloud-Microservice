package com.petrovskiy.epm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateAttributeDto {

    private List<String> tagNameList;
    private String searchPart;

    private String orderSort;
    private List<String> sortingFieldList;
}