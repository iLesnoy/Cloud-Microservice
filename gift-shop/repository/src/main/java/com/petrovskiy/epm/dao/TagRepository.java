package com.petrovskiy.epm.dao;

import com.petrovskiy.epm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
}
