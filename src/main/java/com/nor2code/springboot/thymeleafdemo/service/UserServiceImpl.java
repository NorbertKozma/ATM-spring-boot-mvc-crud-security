package com.nor2code.springboot.thymeleafdemo.service;

import com.nor2code.springboot.thymeleafdemo.dao.UserRepository;
import com.nor2code.springboot.thymeleafdemo.entity.Members;
import com.nor2code.springboot.thymeleafdemo.entity.Roles;
import com.nor2code.springboot.thymeleafdemo.entity.User;
import com.nor2code.springboot.thymeleafdemo.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String theId) {

        Optional<User> result = userRepository.findById(theId);

        User theUser = null;
        if (result.isPresent()) {
            theUser = result.get();
        }
        else {
            //we didn't find the user
            throw new RuntimeException("Did not find user id:\n" + FormatUtils.formatAccountNumber(theId));
        }

        return theUser;
    }

    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }

    @Override
    public void createNewMember(String userId, String plainPassword, int initialAmount, List<String> roles) {

        Members member = new Members();
        member.setUser_id(userId);
        member.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(plainPassword));
        member.setActive(true);

        // Roles
        for (String roleName : roles) {
            Roles role = new Roles();
            role.setRole(roleName);
            role.setMembers(member); // kapcsolat beállítása
            member.getRoles().add(role);
        }

        // User adatok
        User user = new User();
        user.setAmount(initialAmount);
        user.setMembers(member); // kapcsolat beállítása
        member.setUser(user);   // kétirányú kapcsolat

        // Mentés (minden cascade-olva van)
        userRepository.save(user);
    }
}
