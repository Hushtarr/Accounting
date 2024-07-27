package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;
    private final RoleService roleService;
    private final SecurityService securityService;

    public UserController(UserService userService, CompanyService companyService, RoleService roleService, SecurityService securityService) {
        this.userService = userService;
        this.companyService = companyService;
        this.roleService = roleService;
        this.securityService = securityService;
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        UserDto loggedInUser = securityService.getLoggedInUser();
        model.addAttribute("newUser", new UserDto());
        setRoleAndCompanyAttributes(model, loggedInUser);
        return "/user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("newUser") @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            UserDto loggedInUser = securityService.getLoggedInUser();
            setRoleAndCompanyAttributes(model, loggedInUser);
            return "/user/user-create";
        }


        if (userService.emailExists(userDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "A user with this email already exists. Please try with a different email.");
            UserDto loggedInUser = securityService.getLoggedInUser();
            setRoleAndCompanyAttributes(model, loggedInUser);
            return "/user/user-create";
        }

        try {
            userService.save(userDto);
        } catch (Exception e) {
            model.addAttribute("error", "Error saving user: " + e.getMessage());
            UserDto loggedInUser = securityService.getLoggedInUser();
            setRoleAndCompanyAttributes(model, loggedInUser);
            return "/user/user-create";
        }

        return "redirect:/users/list";
    }


    @GetMapping("/update/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {

        UserDto loggedInUser = securityService.getLoggedInUser();
        UserDto userDto = userService.findById(id);

        boolean isOnlyAdmin = userService.isOnlyAdmin(userDto);
        userDto.setOnlyAdmin(isOnlyAdmin);

        model.addAttribute("user", userDto);
        setRoleAndCompanyAttributes(model, loggedInUser);
        return "/user/user-update";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            UserDto loggedInUser = securityService.getLoggedInUser();
            setRoleAndCompanyAttributes(model, loggedInUser);
            return "/user/user-update";
        }


        if (userService.emailExists(userDto.getUsername()) && !userService.findById(userDto.getId()).getUsername().equals(userDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "A user with this email already exists. Please try with a different email.");
            UserDto loggedInUser = securityService.getLoggedInUser();
            setRoleAndCompanyAttributes(model, loggedInUser);
            return "/user/user-update";
        }

        boolean isOnlyAdmin = userService.isOnlyAdmin(userDto);
        userDto.setOnlyAdmin(isOnlyAdmin);

        try {
            userService.update(userDto);
        } catch (Exception e) {
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            UserDto loggedInUser = securityService.getLoggedInUser();
            setRoleAndCompanyAttributes(model, loggedInUser);
            return "/user/user-update";
        }

        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/user/user-list";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {

        model.addAttribute("Title", "Cydeo Accounting-User");
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

    private void setRoleAndCompanyAttributes(Model model, UserDto loggedInUser) {
        List<RoleDto> roles;
        List<CompanyDto> companies;
        RoleDto defaultRole = null;

        if (securityService.isRootUser()) {

            roles = roleService.findAll().stream()
                    .filter(role -> Objects.equals("Admin", role.getDescription()))
                    .collect(Collectors.toList());
            companies = companyService.listAllCompany()
                    .stream()
                    .filter(company -> !"CYDEO".equals(company.getTitle()))
                    .collect(Collectors.toList());
        } else if (securityService.isAdmin()) {

            roles = roleService.findAll().stream()
                    .filter(role -> List.of("Admin", "Manager", "Employee").contains(role.getDescription()))
                    .collect(Collectors.toList());
            companies = List.of(loggedInUser.getCompany());
        } else {

            roles = roleService.findAll();
            companies = List.of(loggedInUser.getCompany());
        }

        // Add roles and companies to the model
        model.addAttribute("userRoles", roles);
        model.addAttribute("companies", companies);

        // Set default role if needed
        if (model.containsAttribute("newUser")) {
            UserDto newUser = (UserDto) model.getAttribute("newUser");
            if (defaultRole != null) {
                newUser.setRole(defaultRole);
                model.addAttribute("newUser", newUser);
            }
        } else if (model.containsAttribute("user")) {
            UserDto user = (UserDto) model.getAttribute("user");
            if (defaultRole != null) {
                user.setRole(defaultRole);
                model.addAttribute("user", user);
            }
        }
    }
}

