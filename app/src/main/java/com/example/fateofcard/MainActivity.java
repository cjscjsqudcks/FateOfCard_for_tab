package com.example.fateofcard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ImageButton gpbtn, stbtn, exbtn, expbtn;
    Intent maplay, intent, playing, item;
    AlertDialog st, a, tu;
    IntroActivity intro = new IntroActivity();
    String uid;
    Vibrator vibrator;
    float volume;
    int tutorialcheck;
    int[] tutorialimgs = {R.drawable.tutorial1_main, R.drawable.tutorial2_item, R.drawable.tutorial3_rule1, R.drawable.tutorial4_play, R.drawable.tutorial5_card, R.drawable.tutorial6_panalty, R.drawable.tutorial7_effect, R.drawable.tutorial8_itemex};
    String[] tutorialtx = {"게임 하는 법", "카드 설명", "공격 카드 설명", "카드 효과", "아이템 설명"};
    long pressedTime;
    private SharedPreferences pre;
    private SharedPreferences.Editor edi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gpbtn = (ImageButton) findViewById(R.id.main_play);
        stbtn = (ImageButton) findViewById(R.id.main_option);
        exbtn = (ImageButton) findViewById(R.id.main_exit);
        expbtn = (ImageButton) findViewById((R.id.main_help));
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        edi = pre.edit();
        if (intro.bgm != null)
            intro.bgm.stop();
        intro.bgm = MediaPlayer.create(this, R.raw.mainbgm);
        playing = new Intent(getApplicationContext(), PlayActivity.class);
        //bgm1.setLooping(true);
        intro.bgm.start();
        intro.bgm.setLooping(true);
        volume = pre.getFloat("bgmvolume", 1);
        intro.bgm.setVolume(volume, volume);
        pressedTime = 0;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        maplay = new Intent(getApplicationContext(), BlueActivity.class);
        item = new Intent(getApplicationContext(), ItemActivity.class);
        gpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout l = (LinearLayout) View.inflate(getApplicationContext(), R.layout.activity_choose, null);
                View gamemode = getLayoutInflater().inflate(R.layout.activity_choose, null);
                AlertDialog.Builder gamemodebuilder = new AlertDialog.Builder(MainActivity.this);
                gamemodebuilder.setView(gamemode);
                intent = new Intent(MainActivity.this, ItemActivity.class);
                final Button create = (Button) gamemode.findViewById(R.id.ChooseActivity_create);
                final Button find = (Button) gamemode.findViewById(R.id.ChooseActivity_find);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                        a.dismiss();
                    }
                });
                find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, BlueActivity.class));
                        a.dismiss();
                    }
                });
                a = gamemodebuilder.create();
                a.show();
                a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        stbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View settings = getLayoutInflater().inflate(R.layout.activity_option, null);
                AlertDialog.Builder settingbuilder = new AlertDialog.Builder(MainActivity.this);
                settingbuilder.setView(settings);
                final ImageButton settingclose = (ImageButton) settings.findViewById(R.id.activity_option_close);
                final ImageButton vibrate = (ImageButton) settings.findViewById(R.id.activity_option_vibrate);
                final ImageButton unvibrate = (ImageButton) settings.findViewById(R.id.activity_option_unvibrate);
                final SeekBar bgmSeekbar = (SeekBar) settings.findViewById(R.id.activity_option_bgmctrl);
                final SeekBar effectSeekbar = (SeekBar) settings.findViewById(R.id.activity_option_effectctrl);
                bgmSeekbar.setProgress(pre.getInt("bgmnvolume", 10));
                effectSeekbar.setProgress(pre.getInt("effectnvolume", 5));
                int vibe = pre.getInt("vibe", 1);
                if (vibe == 0) {
                    vibrate.setImageDrawable(getDrawable(R.drawable.option_vibrater_off));
                    unvibrate.setImageDrawable(getDrawable(R.drawable.option_unvibrate_on));
                }
                vibrate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vibrate.setImageDrawable(getDrawable(R.drawable.option_vibrater_on));
                        unvibrate.setImageDrawable(getDrawable(R.drawable.option_unviberate_off));
                        playing.putExtra("vibe", 1);
                        vibrator.vibrate(500);
                        edi.putInt("vibe", 1);
                        edi.commit();
                    }
                });
                unvibrate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vibrate.setImageDrawable(getDrawable(R.drawable.option_vibrater_off));
                        unvibrate.setImageDrawable(getDrawable(R.drawable.option_unvibrate_on));
                        playing.putExtra("vibe", 0);
                        edi.putInt("vibe", 0);
                        edi.commit();
                    }
                });
                bgmSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int bgm_nvolume = bgmSeekbar.getProgress();
                        float bgm_volume = (float) (1 - (Math.log(bgmSeekbar.getMax() - bgmSeekbar.getProgress()) / Math.log(bgmSeekbar.getMax())));
                        intro.bgm.setVolume(bgm_volume, bgm_volume);
                        item.putExtra("bgmvolume", bgm_volume);
                        edi.putFloat("bgmvolume", bgm_volume);
                        edi.putInt("bgmnvolume", bgm_nvolume);
                        edi.commit();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                effectSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        int effect_nvolume = effectSeekbar.getProgress();
                        float effect_volume = (float) (1 - (Math.log(effectSeekbar.getMax() - effectSeekbar.getProgress()) / Math.log(effectSeekbar.getMax())));

                        playing.putExtra("effectvolume", effect_volume);
                        edi.putFloat("effectvolume", effect_volume);
                        edi.putInt("effectnvolume", effect_nvolume);
                        edi.commit();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                settingclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        st.dismiss();
                    }
                });

                st = settingbuilder.create();
                WindowManager.LayoutParams params = st.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                st.show();
                st.getWindow().setAttributes(params);
                st.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        expbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialcheck = 0;
                View helpView = getLayoutInflater().inflate(R.layout.activity_tutorial, null);
                AlertDialog.Builder helpDialog = new AlertDialog.Builder(MainActivity.this);
                helpDialog.setView(helpView);
                final ImageButton nextbtn = (ImageButton) helpView.findViewById(R.id.tutorial_nextbtn);
                final ImageButton prebtn = (ImageButton) helpView.findViewById(R.id.tutorial_prebtn);
                final ImageButton outbtn = (ImageButton) helpView.findViewById(R.id.tutorial_outbtn);
                final ImageView explain = (ImageView) helpView.findViewById(R.id.tutorial_explain);
                final TextView title = (TextView) helpView.findViewById(R.id.tutorial_title);
                explain.setImageDrawable(getDrawable(tutorialimgs[tutorialcheck]));
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tutorialcheck++;
                        explain.setImageDrawable(getDrawable(tutorialimgs[tutorialcheck]));
                        if (tutorialcheck < 4) {
                            title.setText(tutorialtx[0]);
                        } else {
                            title.setText(tutorialtx[tutorialcheck - 3]);
                        }
                        if (tutorialcheck == 7) {
                            nextbtn.setVisibility(View.INVISIBLE);
                        }
                        if (tutorialcheck > 0 && prebtn.getVisibility() == View.INVISIBLE) {
                            prebtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
                prebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tutorialcheck--;
                        explain.setImageDrawable(getDrawable(tutorialimgs[tutorialcheck]));
                        if (tutorialcheck < 4) {
                            title.setText(tutorialtx[0]);
                        } else {
                            title.setText(tutorialtx[tutorialcheck - 3]);
                        }
                        if (tutorialcheck == 0) {
                            prebtn.setVisibility(View.INVISIBLE);
                        }
                        if (tutorialcheck < 7 && nextbtn.getVisibility() == View.INVISIBLE) {
                            nextbtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
                outbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tu.dismiss();
                    }
                });

                tu = helpDialog.create();
                tu.show();


            }
        });
        exbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intro.bgm.stop();
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());//앱프로세스 종료
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(getApplicationContext(), "한번더 누를 시 종료됩니다", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            long time = System.currentTimeMillis() - pressedTime;
            if (time > 1500) {
                pressedTime = 0;
            } else {
                intro.bgm.stop();
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());//앱프로세스 종료
            }
        }
    }
}