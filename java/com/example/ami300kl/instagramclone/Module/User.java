package com.example.ami300kl.instagramclone.Module;

/**
 * Created by Ami300kl on 18. 01. 2019.
 */

public class User {
    private String user_id;
    private long phone_number;
    private String username;
    private String email;

    public User(String user_id, long phone_number, String username, String email) {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.username = username;
        this.email = email;
    }
    public User()
    {

    }
//Newer change names of methods FIREBASE DOES NOT ALLOW IT !!!!


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
