package com.greenacademy.productstore.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.Enums.Role;

import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.UserRepository;

@Service
public class UserServices {

    private UserRepository userRepository;
    private EmailServices emailServices;

    public UserServices(UserRepository userRepository, EmailServices emailServices) {
        this.userRepository = userRepository;
        this.emailServices = emailServices;
    }

    public void register(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredToken_at = LocalDateTime.now().plusMinutes(1);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setRole(Role.USER);
        user.setVerificationToken(token);
        user.setExpiredToken_at(expiredToken_at);
        userRepository.save(user);
        emailServices.sendEmailVerification(user);
    }

    public User login(String email, String password) throws Exception {
        User user = findByEmail(email);
        if (user == null) {
            throw new Exception("Email or Password is incorrect");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        if (user.getVerified_at() == null) {
            throw new Exception("Please verify your account");
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

    public void changePassword(User user, String currentPassword, String newPassword, String confirmPassword)
            throws Exception {
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

    public void verifyAccount(String token) throws Exception {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (user.getExpiredToken_at() != null && LocalDateTime.now().isAfter(user.getExpiredToken_at())) {
            user.setExpiredToken_at(null);
            user.setVerificationToken(null);
            userRepository.save(user);

            throw new Exception("Verification token has expired. Please request a new one.");
        }

        user.setVerified_at(Instant.now());
        user.setExpiredToken_at(null);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public void forgotPassword(String email) throws Exception {
        User user = findByEmail(email);
        if (user == null) {
            throw new Exception("Email not found");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiredTokenAt = LocalDateTime.now().plusMinutes(3);
        user.setVerificationToken(token);
        user.setExpiredToken_at(expiredTokenAt);
        userRepository.save(user);
        emailServices.sendEmailResetPassword(user);
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) throws Exception {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            throw new Exception("Link has expired. Please request a new one.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("New password and confirm password do not match");
        }

        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        user.setExpiredToken_at(null);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public void verificationResetPassword(String token) throws Exception {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (user.getExpiredToken_at() != null && LocalDateTime.now().isAfter(user.getExpiredToken_at())) {
            user.setExpiredToken_at(null);
            user.setVerificationToken(null);
            userRepository.save(user);

            throw new Exception("Verification token has expired. Please request a new one.");
        }
    }

}
