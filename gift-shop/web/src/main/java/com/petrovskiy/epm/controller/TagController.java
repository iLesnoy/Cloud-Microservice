package com.petrovskiy.epm.controller;

import com.petrovskiy.epm.TagService;
import com.petrovskiy.epm.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TagDto create(@RequestBody TagDto tagDto) {
        TagDto created = tagService.create(tagDto);
        /*hateaosBuilder.setLinks(created);*/
        return created;
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    /*@PreAuthorize("hasAuthority('tags:delete')")*/
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}