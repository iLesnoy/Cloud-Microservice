package com.petrovskiy.epm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateAttributeDto {

    private List<@Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{4,40}$") String> tagNameList;
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{3,20}$")
    private String searchPart;
    @Pattern(regexp = "^(ASC|DESC)\\,?(ASC|DESC)??$")
    private String orderSort;
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{3,20}$")
    private List<String> sortingFieldList;
}