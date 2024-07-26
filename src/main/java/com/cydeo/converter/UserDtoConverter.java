package com.cydeo.converter;

import com.cydeo.dto.UserDto;
import com.cydeo.service.UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter implements Converter<String, UserDto> {

    private final UserService userService;

    public UserDtoConverter(UserService userService) { //e
        this.userService = userService;
    }

    public UserDto convert(String source) {
        if (source == null || source.isBlank())
            return null;
        return userService.findById(Long.valueOf(source));
    }

}