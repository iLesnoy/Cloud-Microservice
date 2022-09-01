package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.GiftCertificateService;
import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/*@Api("Controller GiftCertificateController crud operations")*/
@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    /*private final HateoasBuilder hateoasBuilder;*/

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;

    }

    /*@ApiOperation(value = "GiftCertificateDto", notes = "use giftCertificateDto")*/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /*@PreAuthorize("hasAuthority('certificates:create')")*/
    public GiftCertificateDto create(@RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto created = giftCertificateService.create(giftCertificateDto);
        return created;
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    /*@PreAuthorize("hasAuthority('certificates:update')")*/
    public GiftCertificateDto update(@PathVariable Long id,
                                     @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto updated = giftCertificateService.update(id, giftCertificateDto);
        return updated;
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable Long id) {
        GiftCertificateDto certificateDto = giftCertificateService.findById(id);
        return certificateDto;
    }

    @GetMapping
    public Page<GiftCertificateDto> findByAttributes(GiftCertificateAttributeDto attribute, Pageable pageable) {
        Page<GiftCertificateDto> page = giftCertificateService.searchByParameters(attribute, pageable);
        /*page.getContent().forEach(hateoasBuilder::setLinks);*/
        return page;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /*@PreAuthorize("hasAuthority('certificates:delete')")*/
    public void deleteById(@PathVariable Long id) {
        giftCertificateService.delete(id);
    }
}