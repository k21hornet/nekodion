package com.konekokonekone.nekodion.user.repository;

import com.konekokonekone.nekodion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByAuth0Id(String auth0Id);
}
