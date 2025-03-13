package com.example.staj_deneme.Models;

import java.security.Security;

public class UserModel {
    private int Id;
    private String UserName;
    private String PasswordHash;
    private String Email;



    public UserModel() {
    }

    public UserModel(int id, String userName, String passwordHash, String email) {
        Id = id;
        UserName = userName;
        PasswordHash = passwordHash;
        Email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
