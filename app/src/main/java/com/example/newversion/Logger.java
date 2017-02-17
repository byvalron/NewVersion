package com.example.newversion;

import android.widget.Toast;

/**
 * Created by Виталий on 17.02.2017.
 */

public class Logger {
    private static Logger   _instance;
    private Logger() { }


    public synchronized static Logger getInstance()
    {
        if (_instance == null)
        {
            _instance = new Logger();
        }
        return _instance;
    }

    public void Log (String str){
        Toast.makeText(MainActivity.getAppContext(), str, Toast.LENGTH_SHORT).show();


    }
}
