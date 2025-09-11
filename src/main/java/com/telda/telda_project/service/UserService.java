package com.telda.telda_project.service;

import com.telda.telda_project.dto.UpdateUserProfileRequest;
import com.telda.telda_project.entity.User;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getAllUsersForAdmin();
    void activateUser(Long id);
    void deactivateUser(Long id);
    User updateUserProfile(String email, UpdateUserProfileRequest  request);
}
