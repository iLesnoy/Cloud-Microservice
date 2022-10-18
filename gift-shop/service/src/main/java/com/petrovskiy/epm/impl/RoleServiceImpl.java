package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.RoleService;
import com.petrovskiy.epm.dao.RoleRepository;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.dto.RoleDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.impl.CustomRoleMapperImpl;
import com.petrovskiy.epm.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.petrovskiy.epm.exception.ExceptionCode.*;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final CustomRoleMapperImpl roleMapper;
    private final PrivilegeServiceImpl privilegeService;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, CustomRoleMapperImpl roleMapper,
                           PrivilegeServiceImpl privilegeService, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.privilegeService = privilegeService;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public RoleDto create(RoleDto roleDto) {
        roleRepository.findByName(roleDto.getName()).ifPresent(a -> {
            throw new SystemException(DUPLICATE_NAME);
        });
        Role role = roleMapper.dtoToEntity(roleDto);
        setPrivilegesToRole(role);
        return roleMapper.entityToDto(roleRepository.save(role));
    }

    private void setPrivilegesToRole(Role role) {
        role.setPrivilege(role.getPrivilege()
                .stream()
                .map(privilegeService::createPrivilege)
                .collect(Collectors.toSet()));
    }

    @Transactional
    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        AtomicReference<Role> persistedRole = new AtomicReference<>();
        roleRepository.findById(id).ifPresentOrElse((role) -> {
                    role = roleMapper.dtoToEntity(roleDto);
                    setPrivilegesToRole(role);
                    persistedRole.set(roleRepository.save(role));
                },
                () -> {
                    throw new SystemException(NON_EXISTENT_ENTITY);
                });
        return roleMapper.entityToDto(persistedRole.get());
    }

    @Override
    public RoleDto findById(Long id) {
        return roleRepository.findById(id).map(roleMapper::entityToDto)
                .orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    public Page<RoleDto> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(roleMapper::entityToDto);
    }

    @Override
    public void delete(Long id) {
        roleRepository.findById(id).ifPresentOrElse(a ->
                        userRepository.findFirstByRole_Id(id).ifPresentOrElse(b -> {
                            throw new SystemException(USED_ENTITY);
                        }, () -> roleRepository.deleteById(id))
                , () -> {
                    throw new SystemException(NON_EXISTENT_ENTITY);
                });
    }


}

