package com.example.newversion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class MainActivity extends AppCompatActivity {

    private View thumb, joystick;
    private TextView centerText, textView, pow;

    public static void main(String[] args) throws JSchException {
         Session session = null;
        ChannelExec testChannel = (ChannelExec) session.openChannel("exec");
        testChannel.setCommand("true");
        testChannel.connect();

        testChannel.disconnect();
    }

    public String MoveForward = "echo -e -n \"\\x31\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveLeft = "echo -e -n \"\\x32\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveStop = "echo -e -n \"\\x35\\x2c\\x6f\\x6e\\x2c\" > /dev/ttyUSB0";
    public String MoveRight = "echo -e -n \"\\x33\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";
    public String MoveBack = "echo -e -n \"\\x34\\x2c\\x6f\\x6e\\x2c\\x%1$s\\x2c\\x%2$s\\x2c\" > /dev/ttyUSB0";

    private static final int SCALE = 0xFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thumb = findViewById(R.id.thumb);
        joystick = findViewById(R.id.joystick);
        centerText = (TextView) findViewById(R.id.center_text);
        textView = (TextView) findViewById(R.id.textView);
        pow = (TextView) findViewById(R.id.pow);

        joystick.setOnTouchListener(new View.OnTouchListener() {
            float jRadius = getResources().getDimension(R.dimen.joystick_radius) /2;
            float tRadius = getResources().getDimension(R.dimen.thumb_radius) / 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moveThumb(0, 0);
                    case MotionEvent.ACTION_MOVE:

                        moveThumb(event.getX() - jRadius, event.getY() - jRadius);
                        double angle = getAngle(event.getX() - jRadius, event.getY() - jRadius);
                        double POWER1 = getPower(event.getX() - jRadius, event.getY() - jRadius);
                        final String POWER2 = Integer.toHexString(Integer.parseInt(String.valueOf((int)POWER1))).toUpperCase();

                        final String UP = String.format(MoveForward, POWER2, POWER2);
                        final String LEFT = String.format(MoveLeft, POWER2, POWER2);
                        final String RIGHT = String.format(MoveRight, POWER2, POWER2);
                        final String DOWN = String.format(MoveBack, POWER2, POWER2);

                        final double RADIAN = 180 / Math.PI;
                        final double NEXT = angle * RADIAN;
                        if(NEXT >= 338.5 || NEXT < 22.5 ) {
                            SshWrapper.getInstance().runCommand(RIGHT);
                            System.out.println(RIGHT);
                            textView.setText("Direction: RIGHT");
                        } else if(NEXT >= 22.5 && NEXT < 67.5 ) {
                            textView.setText("Direction: DOWN_RIGHT");
                        } else if(NEXT >= 67.5 && NEXT < 113.5 ) {
                            SshWrapper.getInstance().runCommand(DOWN);
                            System.out.println(DOWN);
                            textView.setText("Direction: DOWN");
                        } else if(NEXT >= 113.5 && NEXT < 158.5 ) {
                            textView.setText("Direction: DOWN_LEFT");
                        } else if(NEXT >= 158.5 && NEXT < 203.5 ) {
                            SshWrapper.getInstance().runCommand(LEFT);
                            System.out.println(LEFT);
                            textView.setText("Direction: LEFT");
                        } else if(NEXT >= 203.5 && NEXT < 248.5 ) {
                            textView.setText("Direction: UP_LEFT");
                        } else if(NEXT >= 248.5 && NEXT < 293.5 ) {
                            SshWrapper.getInstance().runCommand(UP);
                            System.out.println(UP);
                            textView.setText("Direction: UP");
                        } else if(NEXT >= 293.5 && NEXT < 338.5 ) {
                            textView.setText("Direction: UP_RIGHT");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        moveThumb(0, 0);
                        SshWrapper.getInstance().runCommand(MoveStop);
                        System.out.println(MoveStop);
                        textView.setText("Direction: ");
                        break;
                    default:
                }
                return true;
            }
            //Перемещение стика
            public void moveThumb(float x, float y) {
                double angle = getAngle(x, -y);
                double radius = getRadius(x, -y);
                double POWER = getPower(x, -y);
                final double RADIAN = 180 / Math.PI;
                final double NEXT = angle * RADIAN;

                centerText.setText(String.valueOf((int) (NEXT)) + ":"+ Integer.toHexString(Integer.parseInt(String.valueOf((int) (POWER)))).toUpperCase());
                pow.setText(String.valueOf((int) getPower(x) + " : " + (int) getPower(-y)));
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

            // Скоростьпо X Y
            public double getPower(float x) {
                x /= jRadius - tRadius;
                return Math.min(Math.rint(Math.abs(x) * SCALE), SCALE) * Math.signum(x);
            }
        });
    }
}
