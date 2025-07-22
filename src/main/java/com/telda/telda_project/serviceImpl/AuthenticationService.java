package com.telda.telda_project.serviceImpl;

import com.telda.telda_project.dto.LoginRequest;
import com.telda.telda_project.dto.LoginResponse;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse authentication(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException ex){
            throw new RuntimeException("Invalid email or password");
        }
    }



}
