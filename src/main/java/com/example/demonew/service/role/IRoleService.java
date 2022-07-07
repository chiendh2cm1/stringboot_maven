package com.example.demonew.service.role;


import com.example.demonew.model.entity.Role;

public interface IRoleService {
    Iterable<Role> findAll();
}