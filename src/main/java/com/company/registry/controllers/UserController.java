package com.company.registry.controllers;

import com.company.registry.domain.Occupation;
import com.company.registry.domain.User;
import com.company.registry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users/{userId}")
    public User findById(@PathVariable UUID userId) {
        return userService.findById(userId);
    }

    @GetMapping("/users/find")
    public List<User> findByFilter(User filter) {
        return userService.findByFilter(filter);
    }

    @PostMapping("users/add")
    public UUID addUser(User user) {
        validateUser(user);
        return userService.saveUser(user);
    }

    @GetMapping("/users/retired/{userId}")
    public boolean isRetired(@PathVariable UUID userId) {
        return userService.isRetired(userId);
    }

    private void validateUser(User user) {
        if (user.getOccupation().equals(Occupation.STUDENT)) {
            if (user.getAge() < 14) {
                throw new IllegalArgumentException("User is too young to be Student");
            }
        } else if (user.getOccupation().equals(Occupation.WORKER)) {
            if (user.getAge() < 18) {
                throw new IllegalArgumentException("User is too young to be Worker");
            } else if (user.getAge() > 60) {
                if (userService.isRetired(user.getPassport())) {
                    throw new IllegalArgumentException("User is too old to be Worker");
                }
            }
        } else if (user.getOccupation().equals(Occupation.RETIRED)) {
            if (user.getAge() < 65) {
                if (!userService.isRetired(user.getPassport())) {
                    throw new IllegalArgumentException("User is too young to be Worker");
                }
            }
        }
    }
}
