package com.telda.telda_project.Controller;

import com.telda.telda_project.dto.AuthRequest;
import com.telda.telda_project.dto.AuthResponse;
import com.telda.telda_project.dto.LoginRequest;
import com.telda.telda_project.dto.LoginResponse;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.enums.Role;
import com.telda.telda_project.service.UserService;
import com.telda.telda_project.serviceImpl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        Optional<User> existingUser = userService.getUserByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("error, Email already exists");
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(0.0)
                .role(Role.valueOf("CUSTOMER"))
                .build();

        User  savedUser = userService.saveUser(newUser);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());
        return ResponseEntity.ok(authenticationService.authentication(loginRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authentication(request));
    }
}
