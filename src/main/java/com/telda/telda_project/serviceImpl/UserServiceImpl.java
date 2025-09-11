package com.telda.telda_project.serviceImpl;

import com.telda.telda_project.dto.UpdateUserProfileRequest;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.repository.UserRepository;
import com.telda.telda_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllUsersForAdmin(){
        return userRepository.findAll();
    }

    public void activateUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isActive()) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    public void deactivateUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isActive()){
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    public User updateUserProfile(String email, UpdateUserProfileRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        return userRepository.save(user);
    }
}
