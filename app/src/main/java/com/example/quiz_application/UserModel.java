package com.example.quiz_application;

public class UserModel {

    private String name;
    private String email;
    private String role;

    // 🔹 Empty constructor (Firebase mate compulsory)
    public UserModel() {
    }

    // 🔹 Parameterized constructor
    public UserModel(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // 🔹 Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // 🔹 Setters (optional but recommended)
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
