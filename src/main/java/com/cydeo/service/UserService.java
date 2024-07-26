package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    void save(UserDto userDto);

    void update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> findAll();

    void deleteUser(Long id);
}
