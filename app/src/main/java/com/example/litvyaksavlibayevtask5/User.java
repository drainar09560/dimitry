package com.example.litvyaksavlibayevtask5;

public class User {

    String mark, model, type;
    int vintage, counter, reg;

    int id;

    public int getId(){ return id; }

    public void setId(int id) { this.id = id; }

    public User(String mark, String model, String type, int vintage, int counter, int reg, int id) {
        this.mark = mark;
        this.model = model;
        this.type = type;
        this.vintage = vintage;
        this.counter = counter;
        this.reg = reg;
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getReg() {
        return reg;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }

}
