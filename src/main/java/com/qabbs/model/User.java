package com.qabbs.model;

public class User {
    private int id;
    private String name;
    private String password;
    private String headUrl;
    private int auth;

    public User() {

    }
    public User(String name) {
        this.name = name;
        this.password = "";
        this.headUrl = "";
        this.auth = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public int getAuth() { return auth; }

    public void setAuth(int auth) { this.auth = auth; }
}
