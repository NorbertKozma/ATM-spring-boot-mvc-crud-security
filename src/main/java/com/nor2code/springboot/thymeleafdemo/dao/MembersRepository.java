package com.nor2code.springboot.thymeleafdemo.dao;

import com.nor2code.springboot.thymeleafdemo.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<Members, String> {
}
