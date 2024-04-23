package com.example.password_manager;

public class NoteClass {

    int id;
    String name, password, url;

    public NoteClass() {
    }

    public NoteClass(int id, String name, String password, String url) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
