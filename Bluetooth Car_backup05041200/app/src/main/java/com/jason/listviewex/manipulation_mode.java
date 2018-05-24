package com.jason.listviewex;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class manipulation_mode extends Activity {

    private AlertDialog.Builder builder;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private Context context;
    private TextView Title;
    private ImageButton imageButton,imageButton2,imageButton3,imageButton4,imageButton5;
    private BTChatService mChatService;
    private static final String TAG = "Car";
    private String remoteDeviceInfo;
    private BluetoothAdapter btAdapter;
    private int mode;
    private final int ButtonMode=1;
    private final int SensorMode=2;
    private final int ControlMode=3;
    private TextView BTText;
    private Button ButtonLink;
    private String remoteMacAddress;
    private static final String GO_FORWARD="f";
    private static final String GO_BACKWARD="b";
    private static final String TURN_LEFT="l";
    private static final String TURN_RIGHT="r";
    private static final String CAR_STOP="p";
    private static final String Song_1 = "1";
    private static final String Song_2 = "2";
    private static final String Song_3 = "3";
    private static final String Song_4 = "4";
    private static final String Song_OFF = "0";
    private String directionCmd, songCmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manipulation_mode);
        context=this;
        setTitle("Car in Button Mode");

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

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);

        imageButton.setOnClickListener( new ButtonClick());
        imageButton2.setOnClickListener( new ButtonClick());
        imageButton3.setOnClickListener( new ButtonClick());
        imageButton4.setOnClickListener( new ButtonClick());
        imageButton5.setOnClickListener( new ButtonClick());


        //String name = intent.getStringExtra("btdata");
        //Title = (TextView) findViewById(R.id.textView2);
        // Title.setText(name);
        Intent intent = getIntent();
        remoteDeviceInfo = intent.getStringExtra("btdata");
        Toast.makeText(context,remoteDeviceInfo,Toast.LENGTH_SHORT).show();
        context = this;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        mChatService = new BTChatService( context, mHandler);
        mode = ButtonMode;
        BTText = (TextView) findViewById(R.id.textView2);
        BTText.setText(remoteDeviceInfo);
        ButtonLink = (Button) findViewById(R.id.button);
        ButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remoteDeviceInfo != null){
                    remoteMacAddress = remoteDeviceInfo.substring(remoteDeviceInfo.length()-17);
                    BluetoothDevice device = btAdapter.getRemoteDevice(remoteMacAddress);
                    mChatService.connect(device);

                } else {
                    Toast.makeText(context,"No Paired BT device",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    /*   @Override
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
          }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.car_menu, menu);
        return true;
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

    private class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.imageButton:
                    Toast.makeText(context,"car forward",Toast.LENGTH_SHORT).show();
                    directionCmd = GO_FORWARD;
                    sendCMD(directionCmd);
                    break;

                case  R.id.imageButton2:
                    Toast.makeText(context,"car backward",Toast.LENGTH_SHORT).show();
                    directionCmd = GO_BACKWARD;
                    sendCMD(directionCmd);
                    break;

                case  R.id.imageButton3:
                    Toast.makeText(context,"car left",Toast.LENGTH_SHORT).show();
                    directionCmd = TURN_LEFT;
                    sendCMD(directionCmd);
                    break;

                case  R.id.imageButton4:
                    Toast.makeText(context,"car forward",Toast.LENGTH_SHORT).show();
                    directionCmd = TURN_RIGHT;
                    sendCMD(directionCmd);
                    break;

                case  R.id.imageButton5:
                    Toast.makeText(context,"car forward",Toast.LENGTH_SHORT).show();
                    directionCmd = CAR_STOP;
                    sendCMD(directionCmd);
                    break;


            }
        }
    }

    // Sends a Command to remote BT device.
    private void sendCMD(String message) {
        // Check that we're actually connected before trying anything
        int mState = mChatService.getState();
        Log.d(TAG, "btstate in sendMessage =" + mState);

        if (mState != BTChatService.STATE_CONNECTED) {
            Log.d(TAG, "btstate =" + mState);
             Toast.makeText(context, "Bluetooth device is not connected. ", Toast.LENGTH_SHORT).show();
            return;

        } else {
            // Check that there's actually something to send
            if (message.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                byte[] send = message.getBytes();
                mChatService.BTWrite(send);

            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.car_exit:
                finish();
                break;

            case R.id.song1:
                songCmd=Song_1;
                sendCMD(songCmd);
                break;

            case R.id.song2:
                songCmd=Song_2;
                sendCMD(songCmd);
                break;

            case R.id.song3:
                songCmd=Song_3;
                sendCMD(songCmd);
                break;

            case R.id.song4:
                songCmd=Song_4;
                sendCMD(songCmd);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    // The Handler that gets information back from the BluetoothChatService
    //There is no message queue leak problem
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDevice = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(context, "Connected to "+ mConnectedDevice, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(Constants.TOAST),Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_ServerMode:
                    // Toast.makeText(context,"Enter Server accept state.",Toast.LENGTH_SHORT).show();   //display on TextView
                    break;

                case Constants.MESSAGE_ClientMode:
                    //  Toast.makeText(context,"Enter Client connect state.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}