package com.danielgkneto.mcjavabc.flightbooking.dao;

import com.danielgkneto.mcjavabc.flightbooking.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Set;

public interface IUserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    Set<User> findAll();
}
