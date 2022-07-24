package com.example.application.Model;

public class Users {

    private String uId, name , profile;

    public Users() {
    }

    public Users(String uId, String name, String profile) {
        this.uId = uId;
        this.name = name;
        this.profile = profile;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
