package com.nor2code.springboot.thymeleafdemo.service;

import com.nor2code.springboot.thymeleafdemo.dao.MembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembersServiceImpl implements MembersService{

    private MembersRepository membersRepository;

    @Autowired
    public MembersServiceImpl(MembersRepository theMembersRepository) {
        membersRepository = theMembersRepository;
    }

    @Override
    public void deleteById(String theId) {
        membersRepository.deleteById(theId);
    }
}
