package com.danielgkneto.mcjavabc.flightbooking.service;

import com.danielgkneto.mcjavabc.flightbooking.dao.IRoleRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IUserRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService{
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

/*    public void saveUser(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByName("USER")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveAdmin(User user) {
        user.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
        user.setEnabled(true);
        userRepository.save(user);
    }*/
}
