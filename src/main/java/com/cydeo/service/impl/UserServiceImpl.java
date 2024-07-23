package com.cydeo.service.impl;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user= userRepository.findByUsername(username);
        return mapperUtil.convert(user, new UserDto());
    }
}
