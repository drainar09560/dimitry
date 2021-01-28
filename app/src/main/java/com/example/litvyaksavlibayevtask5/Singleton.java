package com.example.litvyaksavlibayevtask5;

public class Singleton {
    private static Singleton instance;

    String value = "";
    public Singleton(String strings){value = strings;}

    public String getValue(){return value;}

    public void setValue(String value){this.value = value;}

    public static Singleton getInstance(String strings){
        if (instance==null)
            instance = new Singleton(strings);
        return instance;
    }
}
