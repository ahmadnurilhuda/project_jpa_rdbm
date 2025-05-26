package com.greenacademy.productstore.services;

import java.time.Instant;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.greenacademy.productstore.models.Enums.Role;
import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.UserRepository;

@Service
public class UserServices {

    private UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        System.out.println("\n\n\nRegistering user with phone: " + user.getPhone() + "\n\n\n");
        System.out.println("\n\n\nRegistering user with password: " + user.getPassword() + "\n\n\n");
        System.out.println("\n\n\nRegistering user with username: " + user.getUsername() + "\n\n\n");
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User login(String email, String password) throws Exception {
        User user = findByEmail(email);
        if (user == null) {
            throw new Exception("Email or Password is incorrect");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
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

    public User getById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void update(User user) {

        User userExisting = userRepository.findById(user.getId()).orElse(null);

        if (userExisting != null) {
            userExisting.setUsername(user.getUsername());
            if (!userExisting.getEmail().equals(user.getEmail())) {
                userExisting.setEmail(user.getEmail());
            }
            userExisting.setPhone(user.getPhone());

            userExisting.setUpdated_at(Instant.now());
            userRepository.save(userExisting);
        }
    }

    public void changePassword(User user, String currentPassword, String newPassword, String confirmPassword) throws Exception {
        User userExisting = userRepository.findById(user.getId()).orElse(null);

        if (userExisting != null) {
            if (!BCrypt.checkpw(currentPassword, userExisting.getPassword())) {
                throw new IllegalArgumentException("Password lama tidak cocok");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Konfirmasi password tidak cocok");
            }
            userExisting.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userExisting.setUpdated_at(Instant.now());
            userRepository.save(userExisting);
        }
    }
}
