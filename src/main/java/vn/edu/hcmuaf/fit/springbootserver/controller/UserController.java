package vn.edu.hcmuaf.fit.springbootserver.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.springbootserver.dto.ChangeEmailRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.ChangePasswordRequest;
import vn.edu.hcmuaf.fit.springbootserver.model.User;
import vn.edu.hcmuaf.fit.springbootserver.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            Authentication authentication,
            @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), userDetails));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(
            authentication.getName(),
            request.getCurrentPassword(),
            request.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-email")
    public ResponseEntity<Void> changeEmail(
            Authentication authentication,
            @Valid @RequestBody ChangeEmailRequest request) {
        userService.changeEmail(
            authentication.getName(),
            request.getCurrentPassword(),
            request.getNewEmail()
        );
        return ResponseEntity.ok().build();
    }
} 