package com.lhl.model;

public class User {
    public String id;
    public String token;
    public String email;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
