package com.example.demonew.controller;

import com.example.demonew.configuration.JwtProvider;
import com.example.demonew.model.dto.LoginRequest;
import com.example.demonew.model.dto.SignUpForm;
import com.example.demonew.model.entity.User;
import com.example.demonew.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
@RestController
@Slf4j
public class AuthController {


    @Autowired
    private IUserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        try {
            if (result.hasErrors()) {
                throw new Exception("body request invalid");
            }

            User user = userService.findByUsername(request.getUsername());
            if (user == null) {
                throw new Exception("User not exist");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new Exception("password incorrect!");
            }

            String jwt = jwtProvider.generateTokenLogin(request.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", jwt);
            response.put("type", "Bearer");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignUpForm user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user1 = new User(user.getUsername(), user.getPassword(), user.getRole());
        if (user.getRole().equals("user")) {
            return new ResponseEntity<>(userService.saveUser(user1), HttpStatus.CREATED);
        }
        if (user.getRole().equals("admin")) {
            return new ResponseEntity<>(userService.saveAdmin(user1), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/validated/username")
    public ResponseEntity<?> validateUserName(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            User userFindByUsername = userService.findByUsername(user.getUsername());
            return new ResponseEntity<>(userFindByUsername, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new User(), HttpStatus.OK);
        }
    }
}