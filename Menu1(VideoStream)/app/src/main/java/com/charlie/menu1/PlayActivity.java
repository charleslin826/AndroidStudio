package com.charlie.menu1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class PlayActivity extends Activity {

    private Context context;
    private TextView textView;
    private VideoView videoView;
    private int playVideo =2;
    private int rtspVideo =3;
    private Uri uri;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        context = this;

        textView = (TextView) findViewById(R.id.textView_id);
        videoView = (VideoView) findViewById(R.id.videoView_id);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);

        int mode = intent.getIntExtra("videoMode",playVideo);
        if(mode == playVideo){
            textView.setText("Play local video. \n");
            String localpath = "android.resource://"+getPackageName()+"/"+R.raw.cartoon1;
            textView.append(localpath);
            uri = Uri.parse(localpath);
        }else{
            textView.setText("Play RTSP video. \n");
            String webPath = getResources().getString(R.string.rtsp);
            textView.append(webPath);
            uri = Uri.parse(webPath);
        }
        mediaController = new MediaController(context);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);

    }
}
