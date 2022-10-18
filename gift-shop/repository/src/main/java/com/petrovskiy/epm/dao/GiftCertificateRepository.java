package com.petrovskiy.epm.dao;

import com.petrovskiy.epm.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.petrovskiy.epm.constants.SqlQuery.FIND_CERTIFICATES_BY_SEARCH_PART;
import static com.petrovskiy.epm.constants.SqlQuery.FIND_CERTIFICATES_BY_TAG_NAMES_AND_SEARCH_PART;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate,Long> {
    @Query(FIND_CERTIFICATES_BY_SEARCH_PART)
    Page<GiftCertificate> findByAttributes(@Param("searchPart") String searchPart, Pageable pageable);

    @Query(FIND_CERTIFICATES_BY_TAG_NAMES_AND_SEARCH_PART)
    Page<GiftCertificate> findByAttributes(@Param("tagNameList") List<String> tagNameList, @Param("tagNumber") long tagNumber
            , @Param("searchPart") String searchPart, Pageable pageable);

    Optional<GiftCertificate> findFirstByTagList_Id(Long tagId);

    Optional<GiftCertificate> findByName(String name);
}
