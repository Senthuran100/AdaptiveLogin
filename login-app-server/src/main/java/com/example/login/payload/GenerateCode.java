package com.example.login.payload;

import javax.validation.constraints.NotBlank;

public class GenerateCode {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
