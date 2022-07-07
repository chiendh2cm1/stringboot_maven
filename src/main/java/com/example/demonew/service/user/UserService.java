package com.example.demonew.service.user;

import com.example.demonew.model.dto.UserPrincipal;
import com.example.demonew.model.entity.Role;
import com.example.demonew.model.entity.User;
import com.example.demonew.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    @Override
    public User saveAdmin(User user) {
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);//Mã hóa pass của người dùng
        user.setPassword(encodePassword);
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "ROLE_ADMIN"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(2L, "ROLE_USER"));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public Page<User> getUserByName(String q, Pageable pageable) {
        if (Objects.equals(q, "")) {
            q = "%%";
        } else {
            q = "%" + q + "%";
        }
        return userRepository.getAllUserByName(q, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Kiểm tra user có tồn tại trong database không.
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrincipal.build(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }
}