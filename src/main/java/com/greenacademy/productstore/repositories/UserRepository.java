package com.greenacademy.productstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.greenacademy.productstore.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User findByUsername(String username);
}
