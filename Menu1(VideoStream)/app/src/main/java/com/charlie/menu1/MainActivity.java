package com.charlie.menu1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private TextView showMenu1,showMenu2,showMenu3;
    private final int groupID_1 = 1,groupID_2 = 2,groupID_3 = 3;
    private final int playVideo = 2, rtspVideo = 3 , dialog1 =4, dialog2 =5,dialog3 =6,dialog4 =7;
    private Context context;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        showMenu1 = (TextView) findViewById(R.id.textView);
        showMenu2 = (TextView) findViewById(R.id.textView2);
        showMenu3 = (TextView) findViewById(R.id.textView3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(groupID_1,1,Menu.NONE,"新增");
        menu.add(groupID_2,playVideo,Menu.NONE,"播放影片");
        menu.add(groupID_2,rtspVideo,Menu.NONE,"RTSP影片");
        menu.add(groupID_3,dialog1,Menu.NONE,"Dialog1");
        menu.add(groupID_3,dialog2,Menu.NONE,"Dialog2");
        menu.add(groupID_3,dialog3,Menu.NONE,"Dialog3");
        menu.add(groupID_3,dialog4,Menu.NONE,"Dialog4");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNum = item.getGroupId();
        if(itemNum == groupID_1){
            showMenu1.setText(item.getItemId()+": "+item.getTitle());
        }else if (itemNum == groupID_2){
            showMenu2.setText(item.getItemId()+": "+item.getTitle());
            Intent intent = new Intent(context, PlayActivity.class);
            if(item.getItemId() == playVideo){
                intent.putExtra("videoMode",playVideo);
                intent.putExtra("title","PlayVideo");
            }else{
                intent.putExtra("videoMode",rtspVideo);
                intent.putExtra("title","RTSPVideo");
            }

            startActivity(intent);

        }else if(itemNum == groupID_3){
            showMenu3.setText(item.getItemId()+": "+item.getTitle()+"\n");

            switch (item.getItemId()){
                case dialog1:
                    showDialog1();
                    break;
                case dialog2:
                    showDialog2();
                    break;
                case dialog3:
                    showDialog3();
                    break;
                case dialog4:
                    showDialog4();
                    break;
            }

        }


        return super.onOptionsItemSelected(item);
    }

    private void showDialog1() {

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Change display");
        builder.setMessage("Please confirm to change the text.");
        showMenu3.append("雙鑑對話框 was pressed.\n");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMenu3.append("OK");
                dialog.dismiss();
            }
        }); // <<這個分號要自己加入 **注意**

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMenu3.append("Cancel");
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void showDialog2() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.button_text1);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(R.string.like_android);
        showMenu3.append("多鑑對話框 was pressed.\n");
        showMenu3.append(getString(R.string.like_android));

        builder.setPositiveButton(R.string.like, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMenu3.append(getString(R.string.like));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dislike, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMenu3.append(getString(R.string.dislike));
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(R.string.no_idea, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMenu3.append(getString(R.string.no_idea));
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showDialog3() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.button_text3);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        showMenu3.append("單選項對話框 was pressed.\n");
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.drink)); //import 泛型

        builder.setSingleChoiceItems(R.array.drink, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,list.get(which),Toast.LENGTH_SHORT).show();
                showMenu3.append(list.get(which));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void showDialog4() {
            builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.button_text4);
            builder.setIcon(android.R.drawable.ic_dialog_info);

            final String[] foodList = getResources().getStringArray(R.array.food);
            final boolean[] checkList = new boolean[foodList.length];

            showMenu3.append("多選項對話框 was pressed.\n");
            showMenu3.append("Your select food: ");


            builder.setMultiChoiceItems(foodList, checkList, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkList[which] = isChecked;
                }
            });

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StringBuilder msg = new StringBuilder();
                    for(int i=0;i < checkList.length;i++){  //檢查哪一個item被打勾，有的話就放到msg(這個是string物件)
                        if(checkList[i]){
                            msg.append(foodList[i]+" ");
                        }
                    }

                    showMenu3.append(msg); //msg.toString() 可加可不加
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();

    }
}
