package com.example.newversion;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Виталий on 20.03.2017.
 */

public class LogSingleton {

    private static LogSingleton   _instance;
    private LogSingleton() { }

    public synchronized static LogSingleton getInstance()
    {
        if (_instance == null)
        {
            _instance = new LogSingleton();
        }
        return _instance;
    }

    List<String> log = new ArrayList();
    public List<String> getLog() {
        return this.log;
    }

    public void clearLog() {
        this.log.clear();
    }

    public void addLog(String str) {
        Log.d("Test", str);
    }

    public void addToListLog(String str) {
        this.log.add(0, str);
        }

}
