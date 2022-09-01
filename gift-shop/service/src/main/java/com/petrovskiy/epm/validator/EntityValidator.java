package com.petrovskiy.epm.validator;

import com.petrovskiy.epm.dto.*;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.transaction.SystemException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.petrovskiy.epm.exception.ExceptionCode.*;

@Component
public class EntityValidator {

    private static final int MIN_PERIOD = 1;
    private static final int MAX_PERIOD = 365;
    private static final String NAME_REGEX = "[\\p{Alpha}А-Яа-я]{2,65}";
    private static final String PRICE_REGEX = "^(\\d+|[\\.\\,]?\\d+){1,2}$";
    private static final String DESCRIPTION_REGEX = "[\\p{Alpha}А-Яа-я\\d-.,:;!?()\" ]{2,225}";
    private static final String PASSWORD_REGEX= "^(?=.*[0-9])(?=.*[a-z]||[A-Z]).{8,20}$";
    private static final Set<String> AVAILABLE_SORT_ORDERS = Set.of("asc", "desc");
    private static final Set<String> AVAILABLE_SORTING_FIELDS = Set.of("id", "name", "description"
            , "price", "duration", "createDate", "lastUpdateDate");
    private static final String PAGE_REGEX = "^\\d+$";


    private boolean isNotNullAndBlank(String field) {
        return Objects.nonNull(field) && !field.isBlank();
    }

    public boolean isTagListValid(List<TagDto> tagDtoList) {
        if (!CollectionUtils.isEmpty(tagDtoList) && isTagNameListValid(tagDtoList)) {
            return true;
        } else if (tagDtoList == null) {
            return false;
        } else return CollectionUtils.isEmpty(tagDtoList) && !isTagNameListValid(tagDtoList);
    }


    private boolean isTagNameListValid(List<TagDto> tagDtoList) {
        return tagDtoList.stream().allMatch(tag -> Objects.nonNull(tag) && isNameValid(tag.getName()));
    }

    public boolean isAttributeDtoValid(GiftCertificateAttributeDto attributeDto) {
        List<String> tagNameList = attributeDto.getTagNameList();
        String searchPart = attributeDto.getSearchPart();
        String orderSort = attributeDto.getOrderSort();
        List<String> sortingFieldList = attributeDto.getSortingFieldList();

        return (CollectionUtils.isEmpty(tagNameList) || tagNameList.stream()
                .allMatch(tagName -> Objects.nonNull(tagName) && isNameValid(tagName)))
                && isDescriptionValid(searchPart)
                && (Objects.isNull(sortingFieldList) || AVAILABLE_SORTING_FIELDS.containsAll(sortingFieldList)
                && (Objects.isNull(orderSort) || AVAILABLE_SORT_ORDERS.contains(orderSort.toLowerCase())));
    }

    public boolean isPriceValid(BigDecimal price) {
        if (Objects.nonNull(price)) {
            return matchPriceToRegex(price);
        } else {
            return false;
        }
    }

    public boolean isRequestOrderDataValid(RequestOrderDto orderDto) {
        return Objects.nonNull(orderDto.getUserId()) && Objects.nonNull(orderDto.getCertificateIdList())
                && orderDto.getCertificateIdList().stream().allMatch(Objects::nonNull);
    }


    /**
     * this method is responsible for checking is the page exists
     * @param pageable - Abstract interface for pagination information(number,size,sort).
     * @param totalNumber - total number of elements from db
     * lastPage = counts the last page based on the passed parameters : divides the total number
     *                    of pages by the page number. (the answer is rounded up)
     * @return  true if the page number is less than the counted lastPage
     */
    public boolean isPageExists(Pageable pageable, Long totalNumber) {
        if (pageable.getPageNumber() == 0) {
            return true;
        }
        long lastPage = (long) Math.ceil((double) totalNumber / pageable.getPageNumber());
        return pageable.getPageNumber() < lastPage;
    }

    private boolean matchPriceToRegex(BigDecimal price) {
        return String.valueOf(price.doubleValue()).matches(PRICE_REGEX);
    }

    public boolean isDurationValid(int duration) {
        return Objects.nonNull(duration)
                ? isDurationRangeValid(duration) : duration == 0 || isDurationRangeValid(duration);
    }

    private boolean isDurationRangeValid(int duration) {
        return duration >= MIN_PERIOD & duration <= MAX_PERIOD;
    }

    public boolean isNameValid(String name) {
        return Objects.nonNull(name) && isStringFieldValid(name, NAME_REGEX);
    }

    public boolean isDescriptionValid(String description) {
        return Objects.nonNull(description) && isStringFieldValid(description, DESCRIPTION_REGEX);
    }

    private boolean isStringFieldValid(String field, String regex) {
        if (isNotNullAndBlank(field) && field.matches(regex)) {
            return true;
        } else return Objects.isNull(field) || (!field.isBlank() && field.matches(regex));
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }

    @SneakyThrows
    public void checkGiftValidation(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto == null) {
            throw new SystemException(EMPTY_OBJECT);
        } else if (!isNameValid(giftCertificateDto.getName())) {
            throw new SystemException(CERTIFICATE_INVALID_NAME);
        } else if (!isDescriptionValid(giftCertificateDto.getDescription())) {
            throw new SystemException(CERTIFICATE_INVALID_DESCRIPTION);
        } else if (!isPriceValid(giftCertificateDto.getPrice())) {
            throw new SystemException(CERTIFICATE_INVALID_PRICE);
        } else if (!isDurationRangeValid(giftCertificateDto.getDuration())) {
            throw new SystemException(CERTIFICATE_INVALID_DURATION);
        } else if (!isTagListValid(giftCertificateDto.getTagDtoList())) {
            throw new SystemException(TAG_INVALID_NAME);
        }
    }

    @SneakyThrows
    public void checkUserValidation(UserDto userDto) {
        if (userDto == null) {
            throw new SystemException(EMPTY_OBJECT);
        } else if (!isNameValid(userDto.getFirstName())) {
            throw new SystemException(USER_INVALID_NAME);
        } else if (!isPasswordValid(userDto.getPassword())) {
            throw new SystemException(USER_INVALID_PASSWORD);
        }
    }

    @SneakyThrows
    public void checkOrderValidation(RequestOrderDto order) {
        if (Objects.isNull(order)) {
            throw new SystemException(EMPTY_OBJECT);
        }
        if (!isRequestOrderDataValid(order)) {
            throw new SystemException(NON_EXISTENT_ENTITY);
        }
    }

}