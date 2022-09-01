package com.petrovskiy.epm;

import com.petrovskiy.epm.dto.TagDto;

public interface TagService extends BaseService<TagDto> {
    TagDto create(TagDto tagDto);

    TagDto update(Long id, TagDto tagDto);

    TagDto findById(Long id);

    void delete(Long id);
}
