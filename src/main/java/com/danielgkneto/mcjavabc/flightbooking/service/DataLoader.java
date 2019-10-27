package com.danielgkneto.mcjavabc.flightbooking.service;

import com.danielgkneto.mcjavabc.flightbooking.dao.IRoleRepository;
import com.danielgkneto.mcjavabc.flightbooking.dao.IUserRepository;
import com.danielgkneto.mcjavabc.flightbooking.entity.Role;
import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;

    @Override
    public void run(String... strings) throws Exception {

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(roleRepository.findByName("ADMIN"));
        adminRoles.add(roleRepository.findByName("USER"));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleRepository.findByName("USER"));

        User user = new User("Admin", "Admin", "admin", "password", "99/99/9999", "admin", "admin@example.com", "999-999-9999", adminRoles);
        userRepository.save(user);

        user = new User("Daniel", "Krause", "danielgkneto", "password", "11/10/1979", "Brazil", "daniel@example.com", "222-222-2222", userRoles);
        userRepository.save(user);

    }
}
