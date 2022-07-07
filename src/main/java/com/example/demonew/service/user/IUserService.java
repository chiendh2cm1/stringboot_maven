package com.example.demonew.service.user;


import com.example.demonew.model.entity.Product;
import com.example.demonew.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends UserDetailsService {
    User findByUsername(String username);

    User saveAdmin(User user);

    User saveUser(User user);

    Page<User> getUserByName(String q, Pageable pageable);

    Optional<User> findById(Long id);

    Page<User> findByName(String name, Pageable pageable);

    User save(User user);

    void remove(Long id);
}