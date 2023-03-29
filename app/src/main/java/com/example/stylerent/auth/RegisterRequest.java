package com.example.stylerent.auth;

public class RegisterRequest {
    final String username;
    final String email;
    final String password;

    public RegisterRequest(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
