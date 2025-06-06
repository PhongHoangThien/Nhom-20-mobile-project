package vn.edu.hcmuaf.fit.springbootserver.service;

import vn.edu.hcmuaf.fit.springbootserver.dto.LoginRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.LoginResponse;
import vn.edu.hcmuaf.fit.springbootserver.dto.RegisterRequest;
import vn.edu.hcmuaf.fit.springbootserver.dto.RegisterResponse;
import vn.edu.hcmuaf.fit.springbootserver.model.User;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    User getProfile(String email);
    User updateProfile(String email, User userDetails);
    void changePassword(String email, String oldPassword, String newPassword);
    void changeEmail(String email, String currentPassword, String newEmail);
    void forgotPassword(String email);
} 