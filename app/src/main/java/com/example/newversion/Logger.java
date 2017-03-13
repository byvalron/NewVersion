package com.example.newversion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Виталий on 17.02.2017.
 */

public class Logger extends AppCompatActivity {
    ListView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_list);
        log = (ListView) findViewById(R.id.log);
        // Создаём пустой массив для хранения имен котов

    }
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

    public String Log (String str){
        final ArrayList<String> comm = new ArrayList<String>();

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, comm);
        // Привяжем массив через адаптер к ListView
        log.setAdapter(adapter);

        comm.add(0, str);
        adapter.notifyDataSetChanged();
       // Toast.makeText(MainActivity.getAppContext(), str, Toast.LENGTH_SHORT).show();
        return str;
    }
}
