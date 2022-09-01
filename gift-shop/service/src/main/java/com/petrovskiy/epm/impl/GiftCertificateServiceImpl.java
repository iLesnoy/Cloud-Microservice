package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.GiftCertificateService;
import com.petrovskiy.epm.dao.GiftCertificateRepository;
import com.petrovskiy.epm.dao.OrderRepository;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.mapper.GiftCertificateAttributeMapper;
import com.petrovskiy.epm.mapper.GiftCertificateMapperImpl;
import com.petrovskiy.epm.mapper.TagMapperImpl;
import com.petrovskiy.epm.model.GiftCertificate;
import com.petrovskiy.epm.model.Tag;
import com.petrovskiy.epm.validator.EntityValidator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.transaction.SystemException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.petrovskiy.epm.exception.ExceptionCode.*;
import static org.springframework.data.domain.Sort.by;


@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateMapperImpl giftMapper = new GiftCertificateMapperImpl();
    private final TagMapperImpl tagMapper = new TagMapperImpl();
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagServiceImpl tagService;
    private EntityValidator validator;
    private final OrderRepository orderRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagServiceImpl tagService,
                                      OrderRepository orderRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftMapper.dtoToGift(giftCertificateDto);
        setTagListCertificate(giftCertificate);
        giftCertificateRepository.save(giftCertificate);
        return giftMapper.giftToDto(giftCertificate);
    }

    private void setTagListCertificate(GiftCertificate certificate) {
        certificate.setTagList(certificate.getTagList().stream().map(tagService::createTag)
                .collect(Collectors.toSet()));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto) {
        validator.checkGiftValidation(giftCertificateDto);
        GiftCertificate persistedCertificate = findCertificateById(id);

        setUpdatedFields(persistedCertificate, giftCertificateDto);
        setUpdatedTagList(persistedCertificate, giftCertificateDto.getTagDtoList());

        giftCertificateRepository.save(persistedCertificate);
        return giftMapper.giftToDto(persistedCertificate);
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        GiftCertificate certificateDto = findCertificateById(id);
        return giftMapper.giftToDto(certificateDto); /*fix mapper */
    }

    @Override
    public Page<GiftCertificateDto> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("command is not supported, please use searchByParameters");
    }

    @SneakyThrows
    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftCertificateRepository.findById(id).orElseThrow(() -> new SystemException((NON_EXISTENT_ENTITY)));
    }

    @SneakyThrows
    @Override
    @Transactional
    public Page<GiftCertificateDto> searchByParameters(GiftCertificateAttributeDto attributeDto, Pageable pageable) {
        if (!validator.isAttributeDtoValid(attributeDto)) {
            throw new SystemException(INVALID_ATTRIBUTE_LIST);
        }
        setDefaultParamsIfAbsent(attributeDto);
        Pageable sortedPageable = buildPageableSort(attributeDto.getSortingFieldList(), attributeDto.getOrderSort(), pageable);
        Page<GiftCertificate> certificatePage = Objects.nonNull(attributeDto.getTagNameList())
                ? giftCertificateRepository.findByAttributes(attributeDto.getTagNameList()
                , attributeDto.getTagNameList().size(), attributeDto.getSearchPart(), sortedPageable)
                : giftCertificateRepository.findByAttributes(attributeDto.getSearchPart(), sortedPageable);
        if (!validator.isPageExists(pageable, certificatePage.getTotalElements())) {
            throw new SystemException(NON_EXISTENT_PAGE);
        }
        return new CustomPage<>(certificatePage.getContent(), certificatePage.getPageable(), certificatePage.getTotalElements())
                .map(giftMapper::giftToDto);
    }

    private void setDefaultParamsIfAbsent(GiftCertificateAttributeDto attributeDto) {
        if (Objects.isNull(attributeDto.getSearchPart())) {
            attributeDto.setSearchPart("");
        }
    }

    private Pageable buildPageableSort(List<String> sortingFieldList, String orderSort, Pageable pageable) {
        Sort.Direction direction = Objects.nonNull(orderSort)
                ? Sort.Direction.fromString(orderSort)
                : Sort.Direction.ASC;
        return CollectionUtils.isEmpty(sortingFieldList)
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "id"))
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()
                , by(direction, sortingFieldList.toArray(String[]::new)));
    }

    @SneakyThrows
    @Override
    @Transactional
    public void delete(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if(optionalGiftCertificate.isPresent()) {
            if (orderRepository.findFirstByCertificateListId(id).isPresent()) {
                throw new SystemException(USED_ENTITY);
            } else {
                giftCertificateRepository.delete(optionalGiftCertificate.get());
            }
        }else throw new SystemException(NON_EXISTENT_ENTITY);
        /*orderRepository.findFirstByCertificateListId(id).ifPresentOrElse(a->giftCertificateRepository.delete(optionalGiftCertificate.get()),
                ()->{throw new SystemException(NON_EXISTENT_ENTITY);});*/
    }

    private void setUpdatedFields(GiftCertificate persistedCertificate, GiftCertificateDto updatedCertificateDto) {
        String name = updatedCertificateDto.getName();
        String description = updatedCertificateDto.getDescription();
        BigDecimal price = updatedCertificateDto.getPrice();
        int duration = updatedCertificateDto.getDuration();
        if (Objects.nonNull(name) && !persistedCertificate.getName().equals(name)) {
            persistedCertificate.setName(name);
        }
        if (Objects.nonNull(description) && !persistedCertificate.getDescription().equals(description)) {
            persistedCertificate.setDescription(description);
        }
        if (Objects.nonNull(price) && !persistedCertificate.getPrice().equals(price)) {
            persistedCertificate.setPrice(price);
        }
        if (duration != 0 && persistedCertificate.getDuration() != duration) {
            persistedCertificate.setDuration(duration);
        }
    }


    private void setUpdatedTagList(GiftCertificate persistedCertificate, List<TagDto> tagDtoList) {
        if (CollectionUtils.isEmpty(tagDtoList)) {
            return;
        }
        Set<Tag> updatedTagSet = tagDtoList.stream()
                .map(tagMapper::dtoToTag)
                .map(tagService::createTag)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        persistedCertificate.setTagList(updatedTagSet);
    }
}