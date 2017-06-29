package com.gdcp.newsclient.ui.activity;

import android.animation.Animator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gdcp.newsclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {
    @BindView(R.id.activity_guide)
    RelativeLayout activityGuide;
    private int[] imgs = {R.drawable.ad_new_version1_img1, R.drawable.ad_new_version1_img2,
            R.drawable.ad_new_version1_img3, R.drawable.ad_new_version1_img4, R.drawable.ad_new_version1_img5, R.drawable.ad_new_version1_img6,
            R.drawable.ad_new_version1_img7};
    private int index = 0;
    private MediaPlayer mediaPlayer;
    @BindView(R.id.guide_img)
    ImageView guideImg;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    private boolean isExitActivity = false;

    @Override
    protected void initData() {
        //开启属性动画
        startAnimation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        playMusic();
    }

    private void playMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.new_version);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.start();
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void startAnimation() {
        guideImg.setScaleX(1f);
        guideImg.setScaleY(1f);
        guideImg.animate().scaleX(1.2f).scaleY(1.2f).setDuration(3000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                index++;
                guideImg.setImageResource(imgs[index % imgs.length]);
                if (!isExitActivity) {
                    startAnimation();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_guide;
    }



    @Override
    protected void initView() {

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }


    @OnClick({R.id.btn_enter, R.id.activity_guide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_enter:
                isExitActivity=true;
                startActivity(true,MainActivity.class);
                break;
            case R.id.activity_guide:
                break;
        }
    }
}
