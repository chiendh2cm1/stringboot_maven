package com.example.demonew.controller;

import com.example.demonew.model.dto.CustomPage;
import com.example.demonew.model.entity.User;
import com.example.demonew.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin("*")
@RestController
@RequestMapping("/users")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<CustomPage> findAllUser(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        try {
            int pageCurrent = page >= 1 ? page - 1 : page;
            Pageable pageable = PageRequest.of(pageCurrent, size);
            Page<User> userPage = userService.getUserByName(q, pageable);
            if (userPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<User> users = userPage.getContent();
            Map<String, Object> paging = new HashMap<>();
            paging.put("currentPage", pageCurrent);
            paging.put("totalPage", userPage.getTotalPages());
            paging.put("totalElements", userPage.getTotalElements());
            paging.put("size", size);
            CustomPage customPage = new CustomPage();
            customPage.setContent(users);
            customPage.setPageable(paging);
            return new ResponseEntity<>(customPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> remove(@PathVariable long id) {
        try {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userService.remove(id);
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserByName(@PathVariable long id) {
        Optional<User> userOptional = userService.findById(id);
        try {
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


}
