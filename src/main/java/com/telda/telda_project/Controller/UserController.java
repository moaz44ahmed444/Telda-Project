package com.telda.telda_project.Controller;

import com.telda.telda_project.dto.UpdateUserProfileRequest;
import com.telda.telda_project.dto.UserProfileResponse;
import com.telda.telda_project.entity.User;
import com.telda.telda_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("name",  user.getName());
        response.put("userId", user.getId());
        response.put("balance", user.getBalance());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userService.getUserByEmail(email);

        UserProfileResponse response = UserProfileResponse.builder()
                .id(userOpt.get().getId())
                .name(userOpt.get().getName())
                .email(userOpt.get().getEmail())
                .phone(userOpt.get().getPhone())
                .active(userOpt.get().isActive())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("profile")
    public ResponseEntity<String> updateProfile(Authentication authentication, @RequestBody UpdateUserProfileRequest request){
        String email = authentication.getName();
        User userOpt = userService.updateUserProfile(email, request);

        return ResponseEntity.ok("Profile updated successfully");
    }
}
