package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.TagService;
import com.petrovskiy.epm.dao.GiftCertificateRepository;
import com.petrovskiy.epm.dao.TagRepository;
import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.mapper.TagMapper;
import com.petrovskiy.epm.mapper.TagMapperImpl;
import com.petrovskiy.epm.model.Tag;
import com.petrovskiy.epm.validator.EntityValidator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.SystemException;

import java.util.Optional;

import static com.petrovskiy.epm.exception.ExceptionCode.*;


@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private TagMapper tagMapper = new TagMapperImpl();
    private EntityValidator entityValidator;
    private GiftCertificateRepository giftCertificateRepository;


    @Autowired
    public TagServiceImpl(TagRepository tagRepository,EntityValidator entityValidator,
                          GiftCertificateRepository giftCertificateRepository) {
        this.tagRepository = tagRepository;
        this.entityValidator = entityValidator;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = createTag(tagMapper.dtoToTag(tagDto));
        return tagMapper.tagToDto(tag);
    }

    @SneakyThrows
    public Tag createTag(Tag tag) {
        if (entityValidator.isNameValid(tag.getName())) {
            return tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag));
        }
        throw new SystemException(TAG_INVALID_NAME);
    }

    @SneakyThrows
    @Override
    public TagDto update(Long id, TagDto tagDto) {
        Tag tag = findTagById(id);
        if (!entityValidator.isNameValid(tag.getName())) {
            throw new SystemException(TAG_INVALID_NAME);
        }
        tagRepository.save(tagMapper.dtoToTag(tagDto));
        return tagDto;
    }

    @SneakyThrows
    @Override
    @Transactional
    public Page<TagDto> findAll(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);
        if (!entityValidator.isPageExists(pageable, tagPage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new PageImpl<>(tagPage.getContent(), tagPage.getPageable(), tagPage.getTotalElements())
                .map(tagMapper::tagToDto);
    }


    @Override
    public TagDto findById(Long id) {
        return tagMapper.tagToDto(findTagById(id));
    }

    @SneakyThrows
    private Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @SneakyThrows
    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            if (giftCertificateRepository.findFirstByTagList_Id(id).isPresent()) {
                throw new SystemException(USED_ENTITY);
            }
            tagRepository.delete(optionalTag.get());
        }else throw new SystemException(NON_EXISTENT_ENTITY);
    }

}
