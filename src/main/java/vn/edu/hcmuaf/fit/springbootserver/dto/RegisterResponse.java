package vn.edu.hcmuaf.fit.springbootserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.edu.hcmuaf.fit.springbootserver.model.User;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String token;
    private User user;
} 