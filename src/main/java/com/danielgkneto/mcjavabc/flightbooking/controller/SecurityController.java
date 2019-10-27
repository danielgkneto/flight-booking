package com.danielgkneto.mcjavabc.flightbooking.controller;

import com.danielgkneto.mcjavabc.flightbooking.dao.IRoleRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IUserRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import com.danielgkneto.mcjavabc.flightbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class SecurityController {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("user", new User());
            return "registration";
        }
        else {
            model.addAttribute("error_message", "Log out before creating a new account!");
            model.addAttribute("return_link", "/");
            return "error";
        }
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        }
        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created.");
            return "login";
        }
    }

    @RequestMapping("/login")
    public String login(Principal principal, Model model) {
        if (principal == null) {
            return "login";
        }
        else {
            model.addAttribute("error_message", "You are already logged in!");
            model.addAttribute("return_link", "/");
            return "error";
        }
    }
}
