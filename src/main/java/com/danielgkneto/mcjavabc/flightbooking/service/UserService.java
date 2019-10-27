package com.danielgkneto.mcjavabc.flightbooking.service;

import com.danielgkneto.mcjavabc.flightbooking.dao.IRoleRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IUserRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.Role;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//    public Long countByEmail(String email) {
//        return userRepository.countByEmail(email);
//    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

//    public Long countByUsername(String username) {
//        return userRepository.countByUsername(username);
//    }

    public void saveUser(User user) {
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findByName("USER"));
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public void saveAdmin(User user) {
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleRepository.findByName("ADMIN"));
        adminRoles.add(roleRepository.findByName("USER"));
        user.setRoles(adminRoles);
        userRepository.save(user);
    }

    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentusername = authentication.getName();
        User user = userRepository.findByUsername(currentusername);
        return user;
    }
}
