package com.telda.telda_project.repository;

import com.telda.telda_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.balance FROM User u WHERE u.id = :userId")
    Double findBalanceById(Long userId);

    //List<User> findAllForAdmin();
}
