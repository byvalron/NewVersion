package com.example.newversion;

/**
 * Created by Виталий on 20.03.2017.
 */

public class SettingSingleton {

    public String hostname = "192.168.1.1";
    public String username = "root";
    public String password = "root";
    public int port = 22;

    public String MoveForward = "echo -e -n \"\\x31\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveLeft = "echo -e -n \"\\x32\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveStop = "echo -e -n \"\\x35\\x2c\\x6f\\x6e\\x2c\" > /dev/ttyUSB0";
    public String MoveRight = "echo -e -n \"\\x33\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveBack = "echo -e -n \"\\x34\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";

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
