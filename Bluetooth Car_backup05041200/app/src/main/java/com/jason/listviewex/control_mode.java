package com.jason.listviewex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class control_mode extends Activity {

    private Context context;
    private AlertDialog.Builder builder;
    private Spinner spinner;
    private ArrayAdapter<String> codeAdapter;
    private TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_control_mode);
        setTitle("Control Mode");

        Intent intent = getIntent();
        String name = intent.getStringExtra("btdata");
        Title = (TextView) findViewById(R.id.textView3);
        Title.setText(name);

        spinner = (Spinner) findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                Toast.makeText(context, "Select :" + obj, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
                });
        String[] data = getResources().getStringArray(R.array.song_name);
        codeAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, data); // 若最後一個參數改成android.R.layout.simple_spinner_item, data 則是呼叫系統預設的，不是我們改過的(文字有改顏色)
        codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(codeAdapter);
/*Object obj= parent.getItemAtPosition(position);
                Toast.makeText(context,"Select :"+obj,Toast.LENGTH_SHORT).show();*/


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

