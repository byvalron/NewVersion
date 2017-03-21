package com.example.newversion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by Виталий on 17.02.2017.
 */

public class SettingActivity extends AppCompatActivity {

    public  EditText host, user, pass,por;
    public Button save;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_list);


        save = (Button) findViewById(R.id.save);

        host = (EditText) findViewById(R.id.hostname);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        por = (EditText) findViewById(R.id.port);

        host.setText("192.168.1.1");
        user.setText("root");
        pass.setText("root");
        por.setText("22");

        save.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                SettingSingleton.getInstance().hostname = SettingActivity.this.host.getText().toString();
                SettingSingleton.getInstance().username = SettingActivity.this.user.getText().toString();
                SettingSingleton.getInstance().password = SettingActivity.this.pass.getText().toString();
                SettingSingleton.getInstance().port = Integer.valueOf(SettingActivity.this.por.getText().toString()).intValue();

                LogSingleton.getInstance().addToListLog(SettingActivity.this.por.getText().toString());
                LogSingleton.getInstance().addToListLog(SettingActivity.this.pass.getText().toString());
                LogSingleton.getInstance().addToListLog(SettingActivity.this.user.getText().toString());
                LogSingleton.getInstance().addToListLog(SettingActivity.this.host.getText().toString());
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_log, menu);
        return true;
    }
    public void onMenuClick(MenuItem item) {
        Intent intent1 = new Intent(SettingActivity.this, LogActivity.class);
        startActivity(intent1);
    }
}
