package com.example.newversion;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Виталий on 17.02.2017.
 */

public class SettingActivity extends AppCompatActivity {

    private static Context context;
    public  EditText host, user, pass,por;
    public Button save;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_list);

        SettingActivity.context = getApplicationContext();

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
                Toast.makeText(SettingActivity.getAppContext(), SettingSingleton.getInstance().hostname, Toast.LENGTH_SHORT).show();

                SettingSingleton.getInstance().username = SettingActivity.this.user.getText().toString();
                Toast.makeText(SettingActivity.getAppContext(), SettingSingleton.getInstance().username , Toast.LENGTH_SHORT).show();

                SettingSingleton.getInstance().password = SettingActivity.this.pass.getText().toString();
                Toast.makeText(SettingActivity.getAppContext(), SettingSingleton.getInstance().password , Toast.LENGTH_SHORT).show();

                SettingSingleton.getInstance().port = Integer.valueOf(SettingActivity.this.por.getText().toString()).intValue();
              //  Toast.makeText(SettingActivity.getAppContext(), SettingSingleton.getInstance().port , Toast.LENGTH_SHORT).show();

            }
        });
    }
    public static Context getAppContext() {
        return SettingActivity.context;
    }
}
