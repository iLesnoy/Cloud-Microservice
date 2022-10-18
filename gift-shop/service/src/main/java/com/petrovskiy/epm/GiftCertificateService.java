package com.petrovskiy.epm;

import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiftCertificateService extends BaseService<GiftCertificateDto> {
    Page<GiftCertificateDto> searchByParameters(GiftCertificateAttributeDto attributeDto, Pageable pageable);
    GiftCertificate findCertificateById(Long id);
}
