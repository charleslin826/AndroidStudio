package com.jason.listviewex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class sensor_mode extends Activity {

    private Context context;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private ImageView imageButton,imageButton2,imageButton3,imageButton4,imageButton5;
    private SensorManager sensorManager;
    private MyAccListen listener;
    private AlertDialog.Builder builder;
    private TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulation_mode);  //
        context=this;
        setTitle("Car in Sensor Mode");
        Intent intent = getIntent();
        String name = intent.getStringExtra("btdata");
        Title = (TextView) findViewById(R.id.textView2);
        Title.setText(name);
        imageView1 = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);

        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.INVISIBLE);
        imageView5.setVisibility(View.INVISIBLE);

        imageButton = (ImageView) findViewById(R.id.imageButton);
        imageButton2 = (ImageView) findViewById(R.id.imageButton2);
        imageButton3 = (ImageView) findViewById(R.id.imageButton3);
        imageButton4 = (ImageView) findViewById(R.id.imageButton4);
        imageButton5 = (ImageView) findViewById(R.id.imageButton5);

        imageButton.setVisibility(View.INVISIBLE);
        imageButton2.setVisibility(View.INVISIBLE);
        imageButton3.setVisibility(View.INVISIBLE);
        imageButton4.setVisibility(View.INVISIBLE);
        imageButton5.setVisibility(View.INVISIBLE);

        int currentOrientation = this.getResources().getConfiguration().orientation;
        if(currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //設定畫面直立
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new MyAccListen();
        sensorManager.registerListener(listener, sensor, sensorManager.SENSOR_DELAY_UI);
    }

    private class MyAccListen implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor: "+event.sensor.getName()+"\n");
            sb.append("values :\n");
            sb.append("X :"+event.values[0]+"\n");
            sb.append("Y :"+event.values[1]+"\n");
            sb.append("Z :"+event.values[2]+"\n");

            float X_value = event.values[0];
            float Y_value = event.values[1];
            float Z_value = event.values[2];

            if( X_value < -2.0){
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageView4.setVisibility(View.VISIBLE);
                imageView5.setVisibility(View.INVISIBLE);
            }else if (X_value>2.0){
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.VISIBLE);
                imageView4.setVisibility(View.INVISIBLE);
                imageView5.setVisibility(View.INVISIBLE);
            }else{
                if(Z_value>5){
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    imageView5.setVisibility(View.INVISIBLE);
                }else if(Z_value<0){
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    imageView5.setVisibility(View.INVISIBLE);
                }else{
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    imageView5.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    //*****Exit button*****//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(1,1,Menu.NONE,"Exit");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); //這邊用code 直接設定 而不用xml設定
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showDialog1();
        return super.onOptionsItemSelected(item);
    }


    private void showDialog1() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Exit");
        builder.setMessage("Sure to exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }); // <<這個分號要自己加入 **注意**
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    //*****Exit button*****//


}
