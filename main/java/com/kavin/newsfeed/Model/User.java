package com.kavin.newsfeed.Model;

public class User {
    private String id;
    private String User;
    private String Pass;
    private String Name;
    private String Phone;
    private String Email;
    private String Image;
    private String status;
    private String search;


    public User() {
    }

    public User(String id, String user, String pass, String name, String phone, String email, String image, String status, String search) {
        id = id;
        User = user;
        Pass = pass;
        Name = name;
        Phone = phone;
        Email = email;
        Image = image;
        status = status;
        search = search;


    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


}
