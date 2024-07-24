package com.cydeo.service.impl;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.User;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;


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
        User user = userRepository.findByUsername(username);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public void save(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        userRepository.save(user);
    }

    @Override
    public void update(UserDto userDto) {
        User user = mapperUtil.convert(userDto, new User());
        userRepository.save(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> mapperUtil.convert(user, new UserDto()))
                .toList();
    }


}
