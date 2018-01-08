package com.gdcp.newsclient.ui.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gdcp.newsclient.R;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class LiveActivity extends AppCompatActivity {
private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        Vitamio.initialize(this);
        //设置视频解码监听
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
         initView();
    }

    private void initView() {
        videoView= (VideoView) findViewById(R.id.videoView);
        String url=getIntent().getStringExtra("url");
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }
}
