package com.example.newversion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Created by Виталий on 20.03.2017.
 */

public class LogActivity extends AppCompatActivity{
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_list);
        ListView lvMain = (ListView) findViewById(R.id.log);
        this.adapter = new ArrayAdapter<>(this,17367043, LogSingleton.getInstance().getLog());
        lvMain.setAdapter(this.adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_clear, menu);
        return true;
    }
    public void onLogMenuClick(MenuItem item) {
        LogSingleton.getInstance().clearLog();
        this.adapter.notifyDataSetChanged();
    }
}
