package com.example.staj_deneme.Models;

import java.security.Security;

public class UserModel {
    private int Id;
    private String UserName;
    private String NormalizedUserName;
    private String Email;
    private String NormalizedEmail;
    private boolean EmailConfirmed;
    private String PasswordHash;
    private String SecurityStamp;
    private String ConcurrencyStamp;
    private String PhoneNumber;
    private boolean PhoneNumberConfirmed;
    private boolean TwoFactorEnabled;
    private String LockoutEnd;
    private boolean LockoutEnabled;
    private int AccessFailedCount;

    public UserModel() {
    }

    public UserModel(int id, String userName, String normalizedUserName, String email, String normalizedEmail, boolean emailConfirmed, String passwordHash, String securityStamp, String concurrencyStamp, String phoneNumber, boolean phoneNumberConfirmed, boolean twoFactorEnabled, String lockoutEnd, boolean lockoutEnabled, int accessFailedCount) {
        Id = id;
        UserName = userName;
        NormalizedUserName = normalizedUserName;
        Email = email;
        NormalizedEmail = normalizedEmail;
        EmailConfirmed = emailConfirmed;
        PasswordHash = passwordHash;
        SecurityStamp = securityStamp;
        ConcurrencyStamp = concurrencyStamp;
        PhoneNumber = phoneNumber;
        PhoneNumberConfirmed = phoneNumberConfirmed;
        TwoFactorEnabled = twoFactorEnabled;
        LockoutEnd = lockoutEnd;
        LockoutEnabled = lockoutEnabled;
        AccessFailedCount = accessFailedCount;
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

    public String getNormalizedUserName() {
        return NormalizedUserName;
    }

    public void setNormalizedUserName(String normalizedUserName) {
        NormalizedUserName = normalizedUserName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNormalizedEmail() {
        return NormalizedEmail;
    }

    public void setNormalizedEmail(String normalizedEmail) {
        NormalizedEmail = normalizedEmail;
    }

    public boolean isEmailConfirmed() {
        return EmailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        EmailConfirmed = emailConfirmed;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public String getSecurityStamp() {
        return SecurityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        SecurityStamp = securityStamp;
    }

    public String getConcurrencyStamp() {
        return ConcurrencyStamp;
    }

    public void setConcurrencyStamp(String concurrencyStamp) {
        ConcurrencyStamp = concurrencyStamp;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberConfirmed() {
        return PhoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(boolean phoneNumberConfirmed) {
        PhoneNumberConfirmed = phoneNumberConfirmed;
    }

    public boolean isTwoFactorEnabled() {
        return TwoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        TwoFactorEnabled = twoFactorEnabled;
    }

    public String getLockoutEnd() {
        return LockoutEnd;
    }

    public void setLockoutEnd(String lockoutEnd) {
        LockoutEnd = lockoutEnd;
    }

    public boolean isLockoutEnabled() {
        return LockoutEnabled;
    }

    public void setLockoutEnabled(boolean lockoutEnabled) {
        LockoutEnabled = lockoutEnabled;
    }

    public int getAccessFailedCount() {
        return AccessFailedCount;
    }

    public void setAccessFailedCount(int accessFailedCount) {
        AccessFailedCount = accessFailedCount;
    }
}
