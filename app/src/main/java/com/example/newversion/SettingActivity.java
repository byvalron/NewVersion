package com.example.newversion;

import android.content.Intent;
import android.content.SharedPreferences;
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

    SharedPreferences sPref, sPrefComm;

    final String SAVED_HOST = "saved_host";
    final String SAVED_USER = "saved_user";
    final String SAVED_PASS = "saved_pass";
    final String SAVED_PORT = "saved_port";

    final String SAVED_COM1 = "saved_com1";
    final String SAVED_COM2 = "saved_com2";
    final String SAVED_COM3 = "saved_com3";
    final String SAVED_COM4 = "saved_com4";
    final String SAVED_COM5 = "saved_com5";

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
                saveText();
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
                saveTextComm();
            }
        });
        loadText();
        loadTextComm();
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sPref.edit();
        ed1.putString(SAVED_HOST, host.getText().toString());
        ed1.putString(SAVED_USER, user.getText().toString());
        ed1.putString(SAVED_PASS, pass.getText().toString());
        ed1.putString(SAVED_PORT, por.getText().toString());
        ed1.commit();
    }
    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);

        String savedHost = sPref.getString(SAVED_HOST, "");
        String savedUser = sPref.getString(SAVED_USER, "");
        String savedPass = sPref.getString(SAVED_PASS, "");
        String savedPort = sPref.getString(SAVED_PORT, "");

        String savedCom1 = sPref.getString(SAVED_COM1, "");
        String savedCom2 = sPref.getString(SAVED_COM2, "");
        String savedCom3 = sPref.getString(SAVED_COM3, "");
        String savedCom4 = sPref.getString(SAVED_COM4, "");
        String savedCom5 = sPref.getString(SAVED_COM5, "");

        host.setText(savedHost);
        user.setText(savedUser);
        pass.setText(savedPass);
        por.setText(savedPort);

        com1.setText(savedCom1);
        com2.setText(savedCom2);
        com3.setText(savedCom3);
        com4.setText(savedCom4);
        com5.setText(savedCom5);
    }
    void saveTextComm() {
        sPrefComm = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed2 = sPrefComm.edit();

        ed2.putString(SAVED_COM1, com1.getText().toString());
        ed2.putString(SAVED_COM2, com2.getText().toString());
        ed2.putString(SAVED_COM3, com3.getText().toString());
        ed2.putString(SAVED_COM4, com4.getText().toString());
        ed2.putString(SAVED_COM5, com5.getText().toString());
        ed2.commit();
    }
    void loadTextComm() {
        sPrefComm = getPreferences(MODE_PRIVATE);

        String savedCom1 = sPrefComm.getString(SAVED_COM1, "");
        String savedCom2 = sPrefComm.getString(SAVED_COM2, "");
        String savedCom3 = sPrefComm.getString(SAVED_COM3, "");
        String savedCom4 = sPrefComm.getString(SAVED_COM4, "");
        String savedCom5 = sPrefComm.getString(SAVED_COM5, "");

        com1.setText(savedCom1);
        com2.setText(savedCom2);
        com3.setText(savedCom3);
        com4.setText(savedCom4);
        com5.setText(savedCom5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
        saveTextComm();
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
