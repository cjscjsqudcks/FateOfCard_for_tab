package com.example.fateofcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fateofcard.model.RoomModel;
import com.example.fateofcard.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ItemActivity extends AppCompatActivity {
    ImageButton play, allcard, addcard, glence, doublecard, allplayer, defence, startbtn, gamebtn;

    ImageView startimage, gameimage;
    LinearLayout playerbg2, playerbg1;
    LinearLayout[] playerbg;
    TextView playertx1, playertx2, playertx2_1;
    TextView[] playertx;
    String uid, gameuid, destinationUid, ready;
    int startitem, gameitem, search, i, check, startgame, type;
    float item;//bgm소리 크기
    static MediaPlayer bgm3;
    //MainActivity main=new MainActivity();
    IntroActivity intro = new IntroActivity();

    long pressedTime;
    int[] startitems = {R.drawable.item_allcard, R.drawable.item_allplayer, R.drawable.item_double};
    int[] gameitems = {R.drawable.item_addcard, R.drawable.item_defence, R.drawable.item_glence};

    Intent getevery, intent;
    private SharedPreferences pre;
    Random r;
    int playercount;

    DatabaseReference reuse;
    String[] players = {"", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        intro.bgm.stop();
        intro.bgm = MediaPlayer.create(this, R.raw.playbgm);
        intro.bgm.start();
        intro.bgm.setLooping(true);
        //main.bgm1.stop();
        //bgm3=MediaPlayer.create(this,R.raw.playbgm);
        //bgm3.setLooping(true);
        //bgm3.start();

        play = (ImageButton) findViewById(R.id.item_play);
        allcard = (ImageButton) findViewById(R.id.item_allcard);
        addcard = (ImageButton) findViewById(R.id.item_addcard);
        glence = (ImageButton) findViewById(R.id.item_glence);
        doublecard = (ImageButton) findViewById(R.id.item_double);
        allplayer = (ImageButton) findViewById(R.id.item_allplayer);
        defence = (ImageButton) findViewById(R.id.item_defence);
        startbtn = (ImageButton) findViewById(R.id.item_startbtn);
        gamebtn = (ImageButton) findViewById(R.id.item_gamebtn);
        startimage = (ImageView) findViewById(R.id.activity_item_startitem);
        gameimage = (ImageView) findViewById(R.id.activity_item_gameitem);
        playerbg1 = (LinearLayout) findViewById(R.id.activity_item_player1_bg);
        playerbg2 = (LinearLayout) findViewById(R.id.activity_item_player2_bg);
        playertx1 = (TextView) findViewById(R.id.activity_item_player1);
        playertx2 = (TextView) findViewById(R.id.activity_item_player2);
        playertx2_1 = (TextView) findViewById(R.id.activity_item_player2_1);
        playertx = new TextView[]{playertx1, playertx2};
        playerbg = new LinearLayout[]{playerbg1, playerbg2};
        pressedTime = 0;
        startgame = 0;
        startitem = gameitem = -1;
        r = new Random();
        intent = new Intent(getApplicationContext(), PlayActivity.class);
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        getevery = getIntent();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        type = getevery.getIntExtra("type", 0);
        item = pre.getFloat("bgmvolume", (float) 1);//getevery.getFloatExtra("bgmvolume",1);
        //Toast.makeText(getApplicationContext(),Float.toString(item),Toast.LENGTH_SHORT).show();
        intro.bgm.setVolume(item, item);

        ready = "0";
        gameuid = getevery.getStringExtra("gameroomUid");
        destinationUid = getevery.getStringExtra("destinationUid");
        reuse = FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameuid);
        /*reuse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //RoomModel.player roomModelplayer=new RoomModel.player();
                //roomModelplayer.uid=uid;
                // RoomModel roomModel=snapshot.getValue(RoomModel.class);
                 //int member=roomModel.players.size();
                 //roomModel.players.put(Integer.toString(member),roomModelplayer);
                 //reuse.child("players").push().setValue(roomModelplayer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameuid).child("players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                check=0;
                playercount=0;
                for(DataSnapshot item:snapshot.getChildren()){
                    //String a=snapshot.child("0").getValue(RoomModel.player.class).uid;
                    if(players[playercount].equals("")){
                        players[playercount]=item.getValue(RoomModel.player.class).uid;
                        playerbg[playercount].setBackground(getDrawable(R.drawable.stayroom_playeron));
                        if(players[playercount].equals(uid)){
                            intent.putExtra("myturn",playercount);
                        }
                        FirebaseDatabase.getInstance().getReference().child("users").child(players[playercount]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String s=snapshot.getValue(UserModel.class).username;
                                playertx[playercount].setText(s);
                                playercount++;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        playercount++;
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot item : snapshot.getChildren()) {
                    String s;
                    if (type == 0) {
                        if (item.getKey().equals(uid)) {
                            s = item.getValue(UserModel.class).username;
                            playertx1.setText(s);
                        } else if (item.getKey().equals(destinationUid)) {
                            s = item.getValue(UserModel.class).username;
                            playertx2.setText(s);
                        }
                    } else {
                        if (item.getKey().equals(destinationUid)) {
                            s = item.getValue(UserModel.class).username;
                            playertx1.setText(s);
                        } else if (item.getKey().equals(uid)) {
                            s = item.getValue(UserModel.class).username;
                            playertx2.setText(s);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        reuse.child("gamestart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String startgames = (String) snapshot.getValue();
                    if (startgames.equals("1") && startgame == 0) {
                        intent.putExtra("gameroomUid", gameuid);
                        intent.putExtra("destinationUid", destinationUid);
                        intent.putExtra("startItem", startitem);
                        intent.putExtra("gameItem", gameitem);
                        startgame = 1;
                        startActivity(intent);
                        finishAndRemoveTask();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (type == 0) {
            play.setImageDrawable(getDrawable(R.drawable.stayroom_playoff2));
            reuse.child("ready").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        ready = (String) snapshot.getValue();
                        if (ready.equals("2")) {
                            play.setImageDrawable(getDrawable(R.drawable.stayroom_play));
                        } else if (ready.equals("1")) {
                            if (playertx2.getVisibility() == View.INVISIBLE) {
                                playertx2_1.setVisibility(View.VISIBLE);
                                playertx2.setVisibility(View.VISIBLE);
                                playerbg2.setBackground(getDrawable(R.drawable.choose_bg2));
                            }

                            play.setImageDrawable(getDrawable(R.drawable.stayroom_playoff2));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ready.equals("2"))
                        reuse.child("gamestart").setValue("1");
                    else {
                        Toast.makeText(getApplicationContext(), "상대방이 아직 준비가 되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            reuse.child("ready").setValue("1");
            ready = "1";
            playertx2_1.setVisibility(View.VISIBLE);
            playertx2.setVisibility(View.VISIBLE);
            playerbg2.setBackground(getDrawable(R.drawable.choose_bg2));
            play.setImageDrawable(getDrawable(R.drawable.stayroom_readyoff2));
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ready.equals("1")) {
                        play.setImageDrawable(getDrawable(R.drawable.stayroom_ready2));
                        reuse.child("ready").setValue("2");
                        ready = "2";
                    } else {
                        play.setImageDrawable(getDrawable(R.drawable.stayroom_readyoff2));
                        reuse.child("ready").setValue("1");
                        ready = "1";
                    }
                }
            });
        }
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = r.nextInt(10);
                if (search == 0) {
                    startitem = 0;
                } else if (search <= 3 && search >= 1) {
                    startitem = 1;
                } else if (search <= 9 && search >= 4) {
                    startitem = 2;
                }
                startimage.setImageDrawable(getDrawable(startitems[startitem]));
            }
        });
        gamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = r.nextInt(10);
                if (search < 1) {
                    gameitem = 1;
                } else if (search < 4 && search > 0) {
                    gameitem = 0;
                } else if (search < 10 && search > 3) {
                    gameitem = 2;
                }
                gameimage.setImageDrawable(getDrawable(gameitems[gameitem]));
            }
        });


        allcard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "내 모든 카드 다시 뽑기", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        allplayer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "모든 플레이어 카드 다시 뽑기", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        doublecard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "내 카드 두배 뽑기", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        addcard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "다음 플레이어의 카드를 추가로 먹이기", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        defence.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "상대의 공격 방어권(카드는 5개까지)", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        glence.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "다른 플레이어의 손에 있는 카드중 한장 보기", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        ;

    }

    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(getApplicationContext(), "한번더 누를 시 메인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            long time = System.currentTimeMillis() - pressedTime;
            if (time > 1500) {
                pressedTime = 0;
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finishAndRemoveTask();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}