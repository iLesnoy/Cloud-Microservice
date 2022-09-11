package com.petrovskiy.epm.dao;

import com.petrovskiy.epm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findFirstByRole_Id(Long roleId); //можно проверит есть ли роль у какого-нить пользователя
}
