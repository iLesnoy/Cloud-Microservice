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


}