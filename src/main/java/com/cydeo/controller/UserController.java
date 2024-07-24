package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user-form";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("user") UserDto userDto) {
        userService.save(userDto);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        UserDto userDto = userService.findById(id);
        model.addAttribute("user", userDto);
        return "user-form";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserDto userDto) {
        userService.update(userDto);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }
}

