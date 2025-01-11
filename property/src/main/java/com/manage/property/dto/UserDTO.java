package com.manage.property.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotNull(message = "please provide valid username")
    @NotEmpty(message = "please provide valid username")
    private String username;

    @NotNull(message = "please provide valid password")
    @NotEmpty(message = "please provide valid password")
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
