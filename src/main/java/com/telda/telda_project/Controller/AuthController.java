package com.telda.telda_project.Controller;

import com.telda.telda_project.dto.AuthRequest;
import com.telda.telda_project.dto.AuthResponse;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public AuthResponse register(@RequestBody AuthRequest request) {
        Optional<User> existingUser = userService.getUserByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return new AuthResponse("User Already Exist", existingUser.get().getId());
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .balance(0.0)
                .build();

        User  savedUser = userService.saveUser(newUser);
        return new AuthResponse("Signup successful", savedUser.getId());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Optional<User> existingUser = userService.getUserByEmail(request.getEmail());
        if (existingUser.isEmpty() || !existingUser.get().getPassword().equals(request.getPassword())) {
            return new AuthResponse("Invalid Credentials", null);
        }

        return new AuthResponse("Login successful", existingUser.get().getId());
    }
}
