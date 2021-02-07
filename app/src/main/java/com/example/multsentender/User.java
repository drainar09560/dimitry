package com.example.multsentender;

public class User {

    String name;
    String surname;

    public User(String name, String surname, String middle, String sex, String smena, int id) {
        this.name = name;
        this.surname = surname;
        this.middle = middle;
        this.sex = sex;
        this.smena = smena;
        this.id = id;
    }

    String middle;
    String sex;
    String smena;

    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSmena() {
        return smena;
    }

    public void setSmena(String smena) {
        this.smena = smena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
