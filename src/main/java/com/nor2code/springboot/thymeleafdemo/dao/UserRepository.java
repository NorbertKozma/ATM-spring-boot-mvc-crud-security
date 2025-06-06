package com.nor2code.springboot.thymeleafdemo.dao;

import com.nor2code.springboot.thymeleafdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
