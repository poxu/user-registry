package com.company.registry.controllers;

import com.company.registry.domain.Occupation;
import com.company.registry.domain.User;
import com.company.registry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//указать общий путь на контроллер
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public User findById(@PathVariable UUID userId) {
        //если нет такого пользователя должен возвращаться  404 NOT FOUND
        return userService.findById(userId);
    }

    @GetMapping("/find")
    public List<User> findByFilter(@RequestParam User filter) {
        return userService.findByFilter(filter);
    }

    //ресурс по которому мы добавляем - должен быть uri коллекции, без add
    @PostMapping("/")
    //необходимо возвращать созданного пользователя, а не uuid
    //пользователя нужно передавать в теле запроса
    //общее требование возвращать DTO, параметры принимать DTO (у нас так было принято)
    //то есть должно быть POJO типа UserDto. но это как вы привыкли...
    public User addUser(@RequestBody User user) {
        //validateUser(user);
        return userService.saveUser(user);
    }

    //возможно нужно включить в ответ по пользователю, чтобы isRetired не приходилось вызывать отдельно
    //лучше придерживаться одинакового шаблона /users/{userId}
    @GetMapping("/{userId}/retired")
    public boolean isRetired(@PathVariable UUID userId) {
        return userService.isRetired(userId);
    }

    //логику нужно:
    //1. убрать из контроллера
    //2. создать валидатор и в него прописать эту логику
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
