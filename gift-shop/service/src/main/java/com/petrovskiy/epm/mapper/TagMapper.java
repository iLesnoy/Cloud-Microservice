package com.petrovskiy.epm.mapper;

import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    Tag dtoToTag(TagDto tagDto);
    TagDto tagToDto(Tag tag);
}
