package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByUsername(String username);

    void save(UserDto userDto);

    void update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> findAll();

    List<UserDto> findByCompanyId(Long companyId); // Add this method

    void deleteUser(Long id);

    boolean emailExists(String email);

    boolean isOnlyAdmin(UserDto userDto);


    boolean isPasswordMatched(String password, String confirmPassword);






}
