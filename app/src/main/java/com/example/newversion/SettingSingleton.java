package com.example.newversion;

/**
 * Created by Виталий on 20.03.2017.
 */

public class SettingSingleton {

    public String hostname = "192.168.1.1";
    public String username = "root";
    public String password = "root";
    public int port = 22;

    private static SettingSingleton instance;
    private SettingSingleton() { }

    public synchronized static SettingSingleton getInstance()
    {
        if (instance == null)
        {
            instance = new SettingSingleton();
        }
        return instance;
    }

}
