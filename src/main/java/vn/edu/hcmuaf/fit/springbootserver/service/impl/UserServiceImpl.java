package vn.edu.hcmuaf.fit.springbootserver.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.springbootserver.config.JwtConfig;
import vn.edu.hcmuaf.fit.springbootserver.dto.*;
import vn.edu.hcmuaf.fit.springbootserver.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.springbootserver.model.Role;
import vn.edu.hcmuaf.fit.springbootserver.model.User;
import vn.edu.hcmuaf.fit.springbootserver.repository.UserRepository;
import vn.edu.hcmuaf.fit.springbootserver.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder,
                         JwtConfig jwtConfig,
                         AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        String token = jwtConfig.generateToken(savedUser);

        return new RegisterResponse(token, savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));
        String token = jwtConfig.generateToken(user);

        return new LoginResponse(token, user);
    }

    @Override
    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public User updateProfile(String email, User userDetails) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        user.setUsername(userDetails.getUsername());
        user.setPhone(userDetails.getPhone());
        user.setAvatar(userDetails.getAvatar());

        return userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changeEmail(String email, String currentPassword, String newEmail) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("New email already exists");
        }

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email) {
        // Implement forgot password logic
    }
} 