package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.TagService;
import com.petrovskiy.epm.dto.TagDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ApiResponse(description = "controller for creationg tags for fist certficate")
@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;
    /*private final HateoasBuilder hateaosBuilder;*/

    @Autowired
    public TagController(TagService tagService/*HateoasBuilder hateaosBuilder*/) {
        this.tagService = tagService;
        /*this.hateaosBuilder = hateaosBuilder;*/
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /*@PreAuthorize("hasAuthority('tags:create')")*/
    public TagDto create(@Valid @RequestBody TagDto tagDto) {
        /*hateaosBuilder.setLinks(created);*/
        return tagService.create(tagDto);
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable Long id) {
        TagDto tagDto = tagService.findById(id);
        /*hateaosBuilder.setLinks(tagDto);*/
        return tagDto;
    }

    @GetMapping
    public Page<TagDto> findAll(Pageable pageable) {
        Page<TagDto> page = tagService.findAll(pageable);
        /*page.getContent().forEach(hateaosBuilder::setLinks);*/
        return page;
    }

    @PatchMapping("/update")
    public TagDto updateTag(@Valid @RequestBody TagDto tagDto){
        return tagService.update(tagDto.getId(),tagDto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /*@PreAuthorize("hasAuthority('tags:delete')")*/
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}