package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.GiftCertificateService;
import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.hateoas.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final HateoasBuilder hateoasBuilder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService,
                                     HateoasBuilder hateoasBuilder) {
        this.giftCertificateService = giftCertificateService;
        this.hateoasBuilder = hateoasBuilder;
    }

    /*@ApiOperation(value = "GiftCertificateDto", notes = "use giftCertificateDto")*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /*@PreAuthorize("hasAuthority('certificates:create')")*/
    public GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto created = giftCertificateService.create(giftCertificateDto);
        return created;
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    /*@PreAuthorize("hasAuthority('certificates:update')")*/
    public GiftCertificateDto update(@PathVariable Long id,
                                     @RequestBody GiftCertificateDto giftCertificateDto) {
        hateoasBuilder.setLinks(giftCertificateService.update(id, giftCertificateDto));
        return giftCertificateService.update(id, giftCertificateDto);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable Long id) {
        hateoasBuilder.setLinks(giftCertificateService.findById(id));
        return giftCertificateService.findById(id);
    }

    @GetMapping
    public Page<GiftCertificateDto> findByAttributes(GiftCertificateAttributeDto attribute, Pageable pageable) {
        Page<GiftCertificateDto> page = giftCertificateService.searchByParameters(attribute, pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /*@PreAuthorize("hasAuthority('certificates:delete')")*/
    public void deleteById(@PathVariable Long id) {
        giftCertificateService.delete(id);
    }
}