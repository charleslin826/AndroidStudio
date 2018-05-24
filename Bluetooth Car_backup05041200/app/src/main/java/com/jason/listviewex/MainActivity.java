package com.jason.listviewex;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends Activity {

    private Context context;
    private Switch sensorSwitch;
    private ListView sensorListView;
    private final int itemID_1 = 0;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;
    private boolean Car_Button_flag, Car_Sensor_flag, Control_flag;
    private String TAG = "Main";
    private int mode;
    private final int ButtonMode = 1;
    private final int SensorMode = 2;
    private final int ControlMode = 3;
    private BluetoothAdapter btAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    private Set<BluetoothDevice> allBTDevices;  //所有註冊過的BT設備
    private ArrayList<String> btDeviceList;
    private final int Permission_REQUEST_CODE = 7;
    private boolean receiverFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        mode = ButtonMode;

        sensorSwitch = (Switch) findViewById(R.id.switch1);
        sensorListView = (ListView) findViewById(R.id.listViewID);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            finish();
        } else if (!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent, REQUEST_ENABLE_BT);
        }

        sensorSwitch.setOnCheckedChangeListener(new sensorChange());


        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemData = parent.getItemAtPosition(position).toString();
                Toast.makeText(context,itemData,Toast.LENGTH_SHORT).show();
                if (Car_Button_flag/* || Car_Sensor_flag*/) {
                    Intent intent = new Intent(context, manipulation_mode.class);
                    intent.putExtra("layout", R.layout.activity_manipulation_mode);
                    intent.putExtra("btdata", itemData);
                    startActivity(intent);
                } else if (Car_Sensor_flag) {
                    Intent intent = new Intent(context, sensor_mode.class);
                    intent.putExtra("layout", R.layout.activity_sensor_mode);
                    intent.putExtra("btdata", itemData);
                    startActivity(intent);
                } else if (Control_flag) {
                    Intent intent = new Intent(context, control_mode.class);
                    intent.putExtra("btdata", itemData);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Please select mode", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);


        menu.add(Menu.NONE, 1, Menu.NONE, "Car_Button_Mode");
        menu.add(Menu.NONE, 2, Menu.NONE, "Car_Sensor_Mode");
        menu.add(Menu.NONE, 3, Menu.NONE, "Control_Mode");
        return super.onCreateOptionsMenu(menu);//true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Car_Button_flag = true;
                Car_Sensor_flag = false;
                Control_flag = false;
                Toast.makeText(context, "Car_Button_Mode", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Car_Sensor_flag = true;
                Car_Button_flag = false;
                Control_flag = false;
                Toast.makeText(context, "Car_Sensor_Mode", Toast.LENGTH_SHORT).show();
                break;
            case 3:
            default:
                Control_flag = true;
                Car_Button_flag = false;
                Car_Sensor_flag = false;
                Toast.makeText(context, "Control_Mode", Toast.LENGTH_SHORT).show();
                break;
            case R.id.discoverable:
                btAdapter.cancelDiscovery();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100); // 100s(秒)
                Toast.makeText(context, "Discoverable to others", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context,"Start to be found", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                Toast.makeText(context, "Refresh_Search", Toast.LENGTH_SHORT).show();
                //***權限請求 PERMISSION_GRANTED - 位置***//
                int permission = ActivityCompat.checkSelfPermission(context, "Manifest.permission.ACCESS_COARSE_LOCATION");
                if(permission != PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, Permission_REQUEST_CODE);
                }
                btAdapter.startDiscovery();
                //***廣播接受器 Broadcast Receiver***//
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                receiverFlag = true ;
                registerReceiver(mReceiver, filter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //***廣播接受器 Broadcast Receiver***//
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(adapter==null){
                    Toast.makeText(context,"Please switch on",Toast.LENGTH_SHORT).show();
                }else {
                    adapter.add("Found :  " + device.getName() + "\n" + device.getAddress());
                }

            }
        }
    };

    private class sensorChange implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                allBTDevices = btAdapter.getBondedDevices(); //所有註冊過的BT設備
                btDeviceList = new ArrayList<String>(); //建一個String型態的ArrayList
                if (allBTDevices.size() > 0) {
                    for (BluetoothDevice device : allBTDevices) {   //foreach method
                        btDeviceList.add("Paired :  " + device.getName() + "\n" + device.getAddress()); //將取得的藍芽裝置放入btDeviceList
                    }

                    adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, btDeviceList); //去這複製"simple_expandable_list_item_1"貼到layout下面即可自訂義_C:\Users\YVTC\AppData\Local\Android\Sdk\platforms\android-26\data\res\layout
                    sensorListView.setAdapter(adapter);
                } else {
                    if (adapter != null) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        }
    }
    //***權限請求 PERMISSION_GRANTED***//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case Permission_REQUEST_CODE:
                if(grantResults[0] == PERMISSION_GRANTED){

                } else{

                }


                return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btAdapter.cancelDiscovery();
        if(mReceiver != null){
            if(receiverFlag){
                unregisterReceiver(mReceiver);
                receiverFlag = false;
            }
        }
    }
}

