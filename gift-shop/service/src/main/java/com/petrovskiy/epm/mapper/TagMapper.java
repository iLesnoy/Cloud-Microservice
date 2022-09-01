package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.model.Tag;
import org.mapstruct.Mapper;


@Mapper
public interface TagMapper {
    Tag dtoToTag(TagDto tagDto);
    TagDto tagToDto(Tag tag);
}
