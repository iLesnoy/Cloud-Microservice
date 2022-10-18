package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.PrivilegeService;
import com.petrovskiy.epm.dao.PrivilegeRepository;
import com.petrovskiy.epm.dto.PrivilegeDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.PrivilegeMapper;
import com.petrovskiy.epm.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

import static com.petrovskiy.epm.exception.ExceptionCode.DUPLICATE_NAME;
import static com.petrovskiy.epm.exception.ExceptionCode.NON_EXISTENT_ENTITY;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;

    @Autowired
    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, PrivilegeMapper privilegeMapper) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeMapper = privilegeMapper;
    }

    @Override
    public PrivilegeDto create(PrivilegeDto privilegeDto) {
        privilegeRepository.findByName(privilegeDto.getName()).ifPresent((privilege) -> {
            throw new SystemException(DUPLICATE_NAME);
        });
        Privilege privilege = privilegeMapper.dtoToPrivilege(privilegeDto);
        return privilegeMapper.privilegeToDto(privilegeRepository.save(privilege));
    }

    @Override
    public PrivilegeDto update(Long id, PrivilegeDto privilegeDto) {
        AtomicReference<Privilege> persistedPrivilege = new AtomicReference<>();
        privilegeRepository.findById(id).ifPresentOrElse(privilege -> {
            privilege = privilegeMapper.dtoToPrivilege(privilegeDto);
            persistedPrivilege.set(privilegeRepository.save(privilege));
        }, () -> {
            throw new SystemException(NON_EXISTENT_ENTITY);
        });
        return privilegeMapper.privilegeToDto(persistedPrivilege.get());
    }

    public Privilege createPrivilege(Privilege privilege) {
        return privilegeRepository.findByName(privilege.getName()).orElseGet(() -> privilegeRepository.save(privilege));
    }

    @Override
    public PrivilegeDto findById(Long id) {
        return privilegeRepository.findById(id)
                .map(privilegeMapper::privilegeToDto)
                .orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    public Page<PrivilegeDto> findAll(Pageable pageable) {
        return privilegeRepository.findAll(pageable).map(privilegeMapper::privilegeToDto);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        PrivilegeDto privilege = findById(id);
        privilegeRepository.delete(privilegeMapper.dtoToPrivilege(privilege));
    }
}
