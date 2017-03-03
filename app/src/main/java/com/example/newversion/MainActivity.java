package com.example.newversion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private View thumb, joystick;
    private ImageButton stop, uu;

    boolean flag = true;

    public String MoveForward = "echo -e -n \"\\x31\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveLeft = "echo -e -n \"\\x32\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveStop = "echo -e -n \"\\x35\\x2c\\x6f\\x6e\\x2c\" > /dev/ttyUSB0";
    public String MoveRight = "echo -e -n \"\\x33\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveBack = "echo -e -n \"\\x34\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";

    private static Context context, cont;
    public static String command;
    private static final int SCALE = 0x55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stop = (ImageButton) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                command = MoveStop;
                new CommandTask().execute();
                System.out.println(MoveStop);
            }
        });

        thumb = findViewById(R.id.thumb);
        joystick = findViewById(R.id.joystick);
        uu = (ImageButton) findViewById(R.id.uu);

        context = this;
        cont = this;
        MainActivity.context = getApplicationContext();

        joystick.setOnTouchListener(new View.OnTouchListener() {
            float jRadius = getResources().getDimension(R.dimen.joystick_radius) /2;
            float tRadius = getResources().getDimension(R.dimen.thumb_radius) / 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:

                        moveThumb(event.getX() - jRadius, event.getY() - jRadius);
                        double angle = getAngle(event.getX() - jRadius, event.getY() - jRadius);
                        double POWER1 = getPower(event.getX() - jRadius, event.getY() - jRadius);
                        final String POWER2 = Integer.toHexString(Integer.parseInt(String.valueOf((int)POWER1))).toUpperCase();

                        final String UP = String.format(MoveForward, POWER2, POWER2);
                        final String UP_LEFT = String.format(MoveForward, 0, POWER2);
                        final String UP_RIGHT = String.format(MoveForward, POWER2, 0);
                        final String LEFT = String.format(MoveLeft, POWER2, POWER2);
                        final String RIGHT = String.format(MoveRight, POWER2, POWER2);
                        final String DOWN = String.format(MoveBack, POWER2, POWER2);
                        final String DOWN_LEFT = String.format(MoveBack, 0, POWER2);
                        final String DOWN_RIGHT = String.format(MoveBack, POWER2, 0);

                        final double RADIAN = 180 / Math.PI;
                        final double NEXT = angle * RADIAN;
                        if(NEXT >= 338.5 || NEXT < 22.5 ) {
                            command = RIGHT;
                            System.out.println(RIGHT);
                        } else if(NEXT >= 22.5 && NEXT < 67.5 ) {
                            command = DOWN_RIGHT;
                            System.out.println(DOWN_RIGHT);
                        } else if(NEXT >= 67.5 && NEXT < 113.5 ) {
                            command = DOWN;
                            System.out.println(DOWN);
                        } else if(NEXT >= 113.5 && NEXT < 158.5 ) {
                            command = DOWN_LEFT;
                            System.out.println(DOWN_LEFT);
                        } else if(NEXT >= 158.5 && NEXT < 203.5 ) {
                            command = LEFT;
                            System.out.println(LEFT);
                        } else if(NEXT >= 203.5 && NEXT < 248.5 ) {
                            command = UP_LEFT;
                            System.out.println(UP_LEFT);
                        } else if(NEXT >= 248.5 && NEXT < 293.5 ) {
                            command = UP;
                            System.out.println(UP);
                        } else if(NEXT >= 293.5 && NEXT < 338.5 ) {
                            command = UP_RIGHT;
                            System.out.println(UP_RIGHT);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        moveThumb(0,0);
                        command = MoveStop;
                        System.out.println(MoveStop);
                    //    moveThumb(event.getX() - jRadius, event.getY() - jRadius);
                    //    TranslateAnimation a = new TranslateAnimation(
                     //           Animation.ABSOLUTE, 0,
                      //          Animation.ABSOLUTE, 0);
                     //   a.setDuration(3000);
                       // a.setFillAfter(true);
                      //  a.setFillBefore(true);
                       // a.setFillEnabled(true);
                      //  thumb.startAnimation(a);
                      //  moveThumb(event.getX() - jRadius, event.getY() - jRadius);

                       // SshWrapper.getInstance().runCommand(MoveStop);
                       // System.out.println(MoveStop);
                        break;
                    default:
                }
                new CommandTask().execute();
                return true;
            }
            //Перемещение стика
            public void moveThumb(float x, float y) {
                double angle = getAngle(x, -y);
                double radius = getRadius(x, -y);

                x = (float) (radius * Math.cos(angle)) * jRadius + joystick.getX() + jRadius - tRadius;
                y = -(float) (radius * Math.sin(angle)) * jRadius + joystick.getY() + jRadius - tRadius;

                thumb.setX(x);
                thumb.setY(y);
            }
            //Угол
            public  double getAngle(float x, float y) {
                double theta;
                x /= jRadius;
                y /= jRadius;
                if (x > 0) {
                    theta = Math.atan(y / x);
                    if (y < 0) {
                        theta += 2 * Math.PI;
                    }
                } else if (x < 0) {
                    theta = Math.atan(y / x) + Math.PI;
                } else {
                    if (y > 0) {
                        theta = Math.PI / 2;
                    } else if (y < 0) {
                        theta = -Math.PI / 2;
                    } else {
                        theta = 0;
                    }
                }
                return theta;
            }
            //Радиус
            public double getRadius(float x, float y) {
                x /= jRadius;
                y /= jRadius;
                return Math.min(Math.sqrt(x * x + y * y), (jRadius - tRadius) / jRadius);
            }
            // Скорость
            public double getPower(float x, float y) {
                x /= jRadius;
                y /= jRadius;
                double radius = (jRadius - tRadius) / jRadius;
                return Math.min(Math.sqrt(x * x + y * y), radius) / radius * SCALE;
            }

        });
        uu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(flag) {
                    new ConnectTask().execute();
                    uu.setBackgroundResource(R.drawable.uuu);
                    Toast.makeText(MainActivity.getAppContext(),"Connect", Toast.LENGTH_SHORT).show();
                    System.out.println("Connect");
                }
                else{
                    new DestroyTask().execute();
                    uu.setBackgroundResource(R.drawable.u);
                    Toast.makeText(MainActivity.getAppContext(),"FALSE!!!", Toast.LENGTH_SHORT).show();
                    System.out.println("FALSE!!!");
                }flag = !flag;
            }
        });
    }
    class ConnectTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String result = SshWrapper.getInstance().firstConnect(context);
            return null;
        }
    }
    class CommandTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String result1 = SshWrapper.getInstance().runCommand(command);
            return null;
        }
    }
    class DestroyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SshWrapper.getInstance().Destroy(cont);
            return null;
        }
    }
    public static Context getAppContext() {
        return MainActivity.context;
    }
}
