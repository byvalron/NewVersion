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

    public  EditText host, user, pass, por, com1, com2, com3, com4, com5;
    public Button save, save2;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_list);

        save = (Button) findViewById(R.id.save);
        save2 = (Button) findViewById(R.id.savecom);

        host = (EditText) findViewById(R.id.hostname);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.passw);
        por = (EditText) findViewById(R.id.port);

        com1 = (EditText) findViewById(R.id.Text);
        com2 = (EditText) findViewById(R.id.Text2);
        com3 = (EditText) findViewById(R.id.Text3);
        com4 = (EditText) findViewById(R.id.Text4);
        com5 = (EditText) findViewById(R.id.Text5);

        host.setText(SettingSingleton.getInstance().hostname);
        user.setText(SettingSingleton.getInstance().username);
        pass.setText(SettingSingleton.getInstance().password);
        por.setText("22");

        com1.setText(SettingSingleton.getInstance().MoveForward);
        com2.setText(SettingSingleton.getInstance().MoveLeft);
        com3.setText(SettingSingleton.getInstance().MoveRight);
        com4.setText(SettingSingleton.getInstance().MoveBack);
        com5.setText(SettingSingleton.getInstance().MoveStop);

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
        save2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                SettingSingleton.getInstance().MoveForward = SettingActivity.this.com1.getText().toString();
                SettingSingleton.getInstance().MoveLeft = SettingActivity.this.com2.getText().toString();
                SettingSingleton.getInstance().MoveRight = SettingActivity.this.com3.getText().toString();
                SettingSingleton.getInstance().MoveBack = SettingActivity.this.com4.getText().toString();
                SettingSingleton.getInstance().MoveStop = SettingActivity.this.com5.getText().toString();

                LogSingleton.getInstance().addToListLog("new " + SettingActivity.this.com1.getText().toString());
                LogSingleton.getInstance().addToListLog("new " + SettingActivity.this.com2.getText().toString());
                LogSingleton.getInstance().addToListLog("new " + SettingActivity.this.com3.getText().toString());
                LogSingleton.getInstance().addToListLog("new " + SettingActivity.this.com4.getText().toString());
                LogSingleton.getInstance().addToListLog("new " + SettingActivity.this.com5.getText().toString());
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
