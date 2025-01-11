package com.manage.property.controller;

import com.manage.property.dto.UserDTO;
import com.manage.property.models.User;
import com.manage.property.services.UserService;
import com.manage.property.util.ApiResponse;
import com.manage.property.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping
    public ResponseEntity<ApiResponse<String>> authenticateUser(@RequestBody @Valid UserDTO userDTO) {
        String username = userDTO.getUsername();

        User user = userService.findByUsername(username);
        if(user != null) {
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                if (user.getRole().contains("ADMIN") || user.getRole().contains("USER")) {

                    String token = jwtUtils.generateToken(username, List.of(user.getRole()));
                    return ResponseEntity.ok(new ApiResponse<>(token, "found"));
                } else {
                    // return unauthorized
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiResponse<>(null, "User is unauthorized"));
                }
            } else {
                // return Invalid password
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(null, "Invalid Credentials"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, "User not found"));
        }
    }

}
