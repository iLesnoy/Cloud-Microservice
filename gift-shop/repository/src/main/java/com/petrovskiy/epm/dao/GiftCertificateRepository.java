package com.petrovskiy.epm.dao;

import com.petrovskiy.epm.model.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate,Long> {
}
