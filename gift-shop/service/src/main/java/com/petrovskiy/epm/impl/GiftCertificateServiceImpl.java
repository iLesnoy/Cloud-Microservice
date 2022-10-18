package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.GiftCertificateService;
import com.petrovskiy.epm.dao.GiftCertificateRepository;
import com.petrovskiy.epm.dao.OrderRepository;
import com.petrovskiy.epm.dto.CustomPage;
import com.petrovskiy.epm.dto.GiftCertificateAttributeDto;
import com.petrovskiy.epm.dto.GiftCertificateDto;
import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.GiftCertificateMapper;
import com.petrovskiy.epm.mapper.TagMapper;
import com.petrovskiy.epm.model.GiftCertificate;
import com.petrovskiy.epm.model.Tag;
import com.petrovskiy.epm.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.petrovskiy.epm.exception.ExceptionCode.*;
import static org.springframework.data.domain.Sort.by;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {


    private final GiftCertificateMapper giftMapper;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagServiceImpl tagService;
    private final TagMapper tagMapper;
    private EntityValidator validator;
    private final OrderRepository orderRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagServiceImpl tagService,
                                      OrderRepository orderRepository, GiftCertificateMapper getGiftMapper,
                                      TagMapper tagMapper) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.orderRepository = orderRepository;
        this.giftMapper = getGiftMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        giftCertificateRepository.findByName(giftCertificateDto.getName()).ifPresent(gift -> {
            throw new SystemException(DUPLICATE_NAME);
        });
        GiftCertificate giftCertificate = giftMapper.dtoToGift(giftCertificateDto);
        setTagListCertificate(giftCertificate);
        return giftMapper.giftToDto(giftCertificateRepository.save(giftCertificate));
    }


    @Override
    @Transactional
    public GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto) {
        AtomicReference<GiftCertificate> giftCertificate = new AtomicReference<>();
        giftCertificateRepository.findById(id).ifPresentOrElse(certificate -> {
            setUpdatedFields(certificate,giftCertificateDto);
            setUpdatedTagList(certificate, giftCertificateDto.getTagDtoList());
            giftCertificate.set(giftCertificateRepository.save(certificate));

        }, () -> {
            throw new SystemException(NON_EXISTENT_ENTITY);
        });
        return giftMapper.giftToDto(giftCertificate.get());
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        return giftCertificateRepository.findById(id).map(giftMapper::giftToDto)
                .orElseThrow(() -> new SystemException((NON_EXISTENT_ENTITY)));
    }

    @Override
    public GiftCertificate findCertificateById(Long id) {
        return giftMapper.dtoToGift(findById(id));
    }

    @Override
    public Page<GiftCertificateDto> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("command is not supported, please use searchByParameters");
    }

    @Override
    @Transactional
    public Page<GiftCertificateDto> searchByParameters(GiftCertificateAttributeDto attributeDto, Pageable pageable) {
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

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        optionalGiftCertificate.ifPresentOrElse(a -> {
            orderRepository.findFirstByCertificateListId(id).ifPresentOrElse(b -> {
                throw new SystemException(USED_ENTITY);
            }, () -> giftCertificateRepository.delete(optionalGiftCertificate.get()));
        }, () -> {
            throw new SystemException(NON_EXISTENT_ENTITY);
        });
    }


    private void setUpdatedTagList(GiftCertificate persistedCertificate, List<TagDto> tagDtoList) {
        Set<Tag> updatedTagSet = tagDtoList.stream()
                .map(tagMapper::dtoToTag)
                .map(tagService::createTag)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        persistedCertificate.setTagList(updatedTagSet);
    }

    private void setUpdatedFields(GiftCertificate persistedCertificate, GiftCertificateDto updatedCertificateDto) {
        String name = updatedCertificateDto.getName();
        String description = updatedCertificateDto.getDescription();
        BigDecimal price = updatedCertificateDto.getPrice();
        int duration = updatedCertificateDto.getDuration();

        persistedCertificate.setName(name);
        persistedCertificate.setDescription(description);
        persistedCertificate.setPrice(price);
        persistedCertificate.setDuration(duration);
    }

    private void setTagListCertificate(GiftCertificate certificate) {
        certificate.setTagList(certificate.getTagList().stream().map(tagService::createTag)
                .collect(Collectors.toSet()));
    }

}