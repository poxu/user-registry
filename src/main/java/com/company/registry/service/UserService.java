package com.company.registry.service;

import com.company.registry.domain.User;
import com.company.registry.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //orElseThrow нужно указать какой Exception создавать. относится ко всем orElseThrow
    public User findById(UUID userId) {
        //лучше возвращать Optional<User>
        return userRepository.findById(userId).orElseThrow();
    }

    public List<User> findByFilter(User filter) {
        return userRepository.findByFilter(
                filter.getId()
                , filter.getName()
                , filter.getSurname()
                , filter.getPatronymic()
                , filter.getOccupation()
                , filter.getAge()
        );
    }

    public UUID saveUser(User user) {
        return userRepository.save(user).getId();
    }

    @Cacheable(value = "retirements")
    public boolean isRetired(String passport) {
        // Этом методе мы на самом деле должны обращаться
        // к внешнему сервису, который отвечает долго
        if ("123".equals(passport)) {
            return false;
        }
        return true;
    }

    public boolean isRetired(UUID userId) {
        return isRetired(findById(userId).getPassport());
    }
}
