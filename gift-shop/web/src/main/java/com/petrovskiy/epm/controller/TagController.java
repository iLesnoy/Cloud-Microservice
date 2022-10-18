package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.TagService;
import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.hateoas.HateoasBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final HateoasBuilder hateoasBuilder;

    @Autowired
    public TagController(TagService tagService,HateoasBuilder hateoasBuilder) {
        this.tagService = tagService;
        this.hateoasBuilder = hateoasBuilder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    /*@PreAuthorize("hasAuthority('tags:create')")*/
    public TagDto create(@Valid @RequestBody TagDto tagDto) {
        TagDto created = tagService.create(tagDto);
        hateoasBuilder.setLinks(created);
        return created;
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable Long id) {
        TagDto tagDto = tagService.findById(id);
        hateoasBuilder.setLinks(tagDto);
        return tagDto;
    }

    @GetMapping
    public Page<TagDto> findAll(Pageable pageable) {
        Page<TagDto> page = tagService.findAll(pageable);
        page.getContent().forEach(hateoasBuilder::setLinks);
        return page;
    }

    @PatchMapping("/update")
    public TagDto updateTag(@Valid @RequestBody TagDto tagDto){
        return tagService.update(tagDto.getId(),tagDto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    /*@PreAuthorize("hasAuthority('tags:delete')")*/
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}