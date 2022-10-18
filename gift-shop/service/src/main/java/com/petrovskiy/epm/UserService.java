package com.petrovskiy.epm;

import com.petrovskiy.epm.dto.ResponseOrderDto;
import com.petrovskiy.epm.dto.UserDto;
import com.petrovskiy.epm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends BaseService<UserDto> {
    User findUserById(Long id);

    UserDto findByName(String name);

    Page<ResponseOrderDto> findUserOrderList(Long id, Pageable pageable);
}
