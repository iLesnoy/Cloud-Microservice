package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.PrivilegeService;
import com.petrovskiy.epm.RoleService;
import com.petrovskiy.epm.dao.RoleRepository;
import com.petrovskiy.epm.dao.UserRepository;
import com.petrovskiy.epm.dto.RoleDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.impl.CustomRoleMapperImpl;
import com.petrovskiy.epm.model.Role;
import lombok.SneakyThrows;
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
    private final PrivilegeService privilegeService;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, CustomRoleMapperImpl roleMapper,
                           PrivilegeService privilegeService, UserRepository userRepository) {
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
        Role role = roleMapper.dtoToRole(roleDto);
        setPrivilegesToRole(role);
        roleRepository.save(role);
        return roleMapper.roleToDto(role);
    }

    private void setPrivilegesToRole(Role role) {
        role.setPrivilege(role.getPrivilege()
                .stream()
                .map(privilegeService::create)
                .collect(Collectors.toSet()));
    }

    @Transactional
    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        roleRepository.findById(id).ifPresentOrElse((a) -> {
                    Role role = roleMapper.dtoToRole(roleDto);
                    setPrivilegesToRole(role);
                    roleRepository.save(role);
                },
                () -> {
                    throw new SystemException(NON_EXISTENT_ENTITY);
                });
        return roleDto;
    }

    /*@Transactional
    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        AtomicReference<Role> role = new AtomicReference<>();
        roleRepository.findById(id).ifPresentOrElse((a) -> {
                    role.set(roleMapper.dtoToRole(roleDto));
                    setPrivilegesToRole(role.get());
                    roleRepository.save(role.get());
                },
                () -> {
                    throw new SystemException(NON_EXISTENT_ENTITY);
                });
        return roleMapper.roleToDto(role.get());
    }*/

    @Override
    public RoleDto findById(Long id) {
        return roleRepository.findById(id).map(roleMapper::roleToDto)
                .orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    public Page<RoleDto> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable).map(roleMapper::roleToDto);
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        userRepository.findFirstByRole_Id(id).ifPresentOrElse(a -> { //не обрабатывается ошибка с non_exist?
                    throw new SystemException(USED_ENTITY);
                },
                () -> roleRepository.deleteById(id));
    }
}
