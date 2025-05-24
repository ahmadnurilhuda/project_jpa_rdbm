package com.greenacademy.productstore.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.Enums.Role;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.UserRepository;

@Service
public class UserServices {

    private UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        System.out.println("\n\n\nRegistering user with phone: " + user.getPhone()+"\n\n\n");
        System.out.println("\n\n\nRegistering user with password: "+user.getPassword()+"\n\n\n");
        System.out.println("\n\n\nRegistering user with username: "+user.getUsername()+"\n\n\n");
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User login (String email, String password) throws Exception {
        User user = findByEmail(email);
        if(user==null) {
            throw new Exception("Email or Password is incorrect");
        }

        if(!BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("Invalid credentials");
        }
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
}
