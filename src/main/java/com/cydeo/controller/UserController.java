package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.UserService;
import com.cydeo.service.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserController(UserService userService, CompanyService companyService, RoleService roleService) {
        this.userService = userService;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.findAll());
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("newUser") UserDto userDto) {
        userService.save(userDto);
        return "redirect:/users/create";
    }

    @GetMapping("/update/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserDto userDto = userService.findById(id);
        model.addAttribute("user", userDto);
        return "/user/user-update";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserDto userDto) {
        userService.update(userDto);
        return "redirect:/users/create";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserDto loggedInUser = userService.findByUsername(username);
        model.addAttribute("users", userService.findByCompanyId(loggedInUser.getCompany().getId()));
        return "/user/user-list";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {

        model.addAttribute("Title","Cydeo Accounting-User");
        model.addAttribute("userRoles", roleService.findAll());
        model.addAttribute("companies", companyService.listAllCompany());

    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        UserDto userDto = userService.findById(id);
        if (userDto.isOnlyAdmin()) {
            model.addAttribute("error", "Cannot delete the only admin of the company.");
            return "redirect:/users/list";
        }
        userService.deleteUser(id);
        return "redirect:/users/list";
    }


}

