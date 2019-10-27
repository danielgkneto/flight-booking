package com.danielgkneto.mcjavabc.flightbooking.dao;

import com.danielgkneto.mcjavabc.flightbooking.entity.Role;
import org.springframework.data.repository.CrudRepository;
import java.util.Set;

public interface IRoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
    Set<Role> findAll();
}
