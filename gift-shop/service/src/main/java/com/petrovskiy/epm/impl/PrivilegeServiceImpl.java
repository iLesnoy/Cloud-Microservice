package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.PrivilegeService;
import com.petrovskiy.epm.dao.PrivilegeRepository;
import com.petrovskiy.epm.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public Privilege create(Privilege privilege) {
        return privilegeRepository.findByName(privilege.getName())
                .orElseGet(()->privilegeRepository
                        .save(privilege));
    }

    @Override
    public Privilege update(Long id, Privilege privilege) {
        return null;
    }

    @Override
    public Privilege findById(Long id) {
        return null;
    }

    @Override
    public Page<Privilege> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
