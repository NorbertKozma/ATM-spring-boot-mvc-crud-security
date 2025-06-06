package com.nor2code.springboot.thymeleafdemo.service;

import com.nor2code.springboot.thymeleafdemo.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(String theId);

    User save(User theUser);

    void createNewMember(String userId, String plainPassword, int initialAmount, List<String> roles);

    // void deleteById(String theId);

//    List<User> findById(String theId);
}
