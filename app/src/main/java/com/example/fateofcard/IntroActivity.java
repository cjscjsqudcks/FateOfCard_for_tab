package com.example.fateofcard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    Intent intent;
    TextView text;
    Thread twinkle;
    static MediaPlayer bgm;
    private SharedPreferences pre;
    float i, volume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        bgm = MediaPlayer.create(this, R.raw.introbgm);
        bgm.setLooping(true);
        bgm.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//전체화면으로 만들기
        text = (TextView) findViewById(R.id.twinkle);
        i = 1;
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        volume = pre.getFloat("bgmvolume", (float) 0.5);
        bgm.setVolume(volume, volume);
        twinkle = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setAlpha(i);
                            }
                        });
                        Thread.sleep(170);
                        if (i > 0) {
                            i -= 0.2;
                        } else {
                            i = 1;
                        }
                    } catch (Throwable t) {

                    }

                }
            }
        });
        twinkle.start();

        intent = new Intent(getApplicationContext(), LoginActivity.class);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startActivity(intent);
                finish();

        }
        return super.onTouchEvent(event);
    }

}
