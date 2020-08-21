package com.company.registry.controllers;

import com.company.registry.domain.Occupation;
import com.company.registry.domain.User;
import com.company.registry.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    private UUID userId = UUID.randomUUID();
    private User john;
    private User sarah;
    private User kyle;


    @BeforeAll
    public void init() {
        john = new User();
        john.setName("John");
        john.setSurname("Connor");
        User saved = userRepository.save(john);
        userId = saved.getId();


        sarah = new User();
        sarah.setName("Sarah");
        sarah.setSurname("Connor");
        userRepository.save(sarah);

        kyle = new User();
        kyle.setName("Kyle");
        kyle.setSurname("Reese");
        userRepository.save(kyle);
    }

    @Test
    void findById() {
        User actual = userController.findById(userId);
        assertEquals(actual, john);
    }

    @Test
    void findByFilter() {
        User filter = new User();
        filter.setSurname("Connor");
        List<User> users = userController.findByFilter(filter);
        assertEquals(2, users.size());
    }

    @Test
    void addUser() {
        User user = new User();
        user.setName("Marcus");
        user.setSurname("Wright");
        user.setAge(30);
        user.setOccupation(Occupation.WORKER);

        UUID newUserId = userController.addUser(user);

        final var actual = userRepository.findById(newUserId).orElseThrow();

        assertEquals(user, actual);
    }

    @Test
    void addInvalidStudent() {
        User user = new User();
        user.setName("Serena");
        user.setSurname("Kogan");
        user.setAge(7);
        user.setOccupation(Occupation.STUDENT);

        assertThrows(IllegalArgumentException.class, () -> userController.addUser(user));

        user.setAge(15);
        user.setOccupation(Occupation.WORKER);
        assertThrows(IllegalArgumentException.class, () -> userController.addUser(user));

        user.setAge(65);
        user.setOccupation(Occupation.WORKER);
        assertThrows(IllegalArgumentException.class, () -> userController.addUser(user));

        user.setAge(64);
        user.setOccupation(Occupation.RETIRED);
        user.setPassport("123");
        assertThrows(IllegalArgumentException.class, () -> userController.addUser(user));
    }
}
