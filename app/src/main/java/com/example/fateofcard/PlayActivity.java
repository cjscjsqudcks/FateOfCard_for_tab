package com.example.fateofcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fateofcard.model.RoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class PlayActivity extends AppCompatActivity {

    private LinearLayoutManager mlay;

    RecyclerView carddeck;
    LinearLayout player1, player2, player3;
    LinearLayout[] playerbg = {player1, player2, player3};
    TextView playercards1, playercards2, playercards3, stacking, turnname;
    TextView[] playercards;
    AlertDialog tutorial;
    RoomModel.handcards chat2;
    ArrayList<RoomModel.handcards> some = new ArrayList<>();
    int[] cards = {R.drawable.card_spade1, R.drawable.card_spade2, R.drawable.card_spade3, R.drawable.card_spade4, R.drawable.card_spade5, R.drawable.card_spade6, R.drawable.card_spade7, R.drawable.card_spade8, R.drawable.card_spade9, R.drawable.card_spade10, R.drawable.card_spade11, R.drawable.card_spade12, R.drawable.card_spade13, R.drawable.card_dia1, R.drawable.card_dia2, R.drawable.card_dia3, R.drawable.card_dia4, R.drawable.card_dia5, R.drawable.card_dia6, R.drawable.card_dia7, R.drawable.card_dia8, R.drawable.card_dia9, R.drawable.card_dia10, R.drawable.card_dia11, R.drawable.card_dia12, R.drawable.card_dia13, R.drawable.card_heart1, R.drawable.card_heart2, R.drawable.card_heart3, R.drawable.card_heart4, R.drawable.card_heart5, R.drawable.card_heart6, R.drawable.card_heart7, R.drawable.card_heart8, R.drawable.card_heart9, R.drawable.card_heart10, R.drawable.card_heart11, R.drawable.card_heart12, R.drawable.card_heart13, R.drawable.card_clover1, R.drawable.card_clover2, R.drawable.card_clover3, R.drawable.card_clover4, R.drawable.card_clover5, R.drawable.card_clover6, R.drawable.card_clover7, R.drawable.card_clover8, R.drawable.card_clover9, R.drawable.card_clover10, R.drawable.card_clover11, R.drawable.card_clover12, R.drawable.card_clover13, R.drawable.card_bjoker, R.drawable.card_cjoker};

    int[] carduse = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};//카드가 현재 누군가의 손에 있는가
    int seleccheck, cardcheck, myturn, cardfinish, cardset, cardused, itemnum, whos, startitem, gameitem, setbyme, vibe, gamestart;
    static MediaPlayer cardshuffle, cardplace, cardslide;
    String save, destinationuid, removecard;

    long pressedTime;
    int mstate;//현재 카드 어떤  것이 있는 지 저장
    int selected = 1;
    private SharedPreferences pre;
    RoomModel chatModel;
    private Handler mHandler;
    String read, uid, gameRoomuid, turn, turntime;
    long notdouble, notdouble2;
    int po, cardon, stack;//카드를 낼시에 선택 삭제
    int ramdomNum;//카드 뽑을 때 사용되는 랜덤숫자 저장용

    float effect_volume;

    Vibrator vibrator;
    AlertDialog seven, abd, abcd;
    ImageButton card, draw, turnover, startitembtn, gameitembtn;
    RoomModel roomModel, roomModel34;
    RecyclerImageTextAdapter cardAdap = null;
    ArrayList<RecyclerItem> cardlist = new ArrayList<RecyclerItem>();
    ArrayList<RoomModel.somebody> somebodyArrayList;
    ArrayList<RoomModel.handcards> handcardsArrayList, mycardArrayList, destiniArrayList;
    int[] startitems = {R.drawable.item_allcard, R.drawable.item_allplayer, R.drawable.item_double};
    int[] startoffitems = {R.drawable.item_allcardoff, R.drawable.item_allplayeroff, R.drawable.item_doubleoff};
    int[] gameitems = {R.drawable.item_addcard, R.drawable.item_defence, R.drawable.item_glence};
    int[] gameoffitems = {R.drawable.item_addcardoff, R.drawable.item_defenceoff, R.drawable.item_glenceoff};
    AlertDialog glenceDialog, resultDialog;

    Intent getEvery;

    Runnable re;

    Thread AcceptThread, SendThread, ConnectedThread, ConnectThread, msAcceptThread, miAcceptThread, mConnectedThread, mConnectThread;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playercards = new TextView[]{playercards1};
        gamestart = 1;
        pressedTime = 0;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        cardshuffle = MediaPlayer.create(this, R.raw.cardshuffle);
        cardslide = MediaPlayer.create(this, R.raw.cardslide8);
        cardplace = MediaPlayer.create(this, R.raw.cardplace2);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myturn = 0;//내순서를 알기 위해 저장하는 곳
        Random r = new Random();
        cardset = -1;//현재 나의 카드의 종류
        cardused = r.nextInt(54);//상대방이 카드에 대한 정보를 넘겼는가
        cardfinish = 0;//카드를 나눠 줬는가
        mstate = -1;//현재 놓여진 카드 번호
        stack = 0;
        cardon = 0;//상대방이 7을 내어 값의 변동이 있었나 확인
        seleccheck = 0;//다른카드가 눌려 있는 지 확인
        cardcheck = -1;//카드가 눌렸는 지 안눌렸는 지 구분
        somebodyArrayList = new ArrayList<>();
        mycardArrayList = new ArrayList<>();
        destiniArrayList = new ArrayList<>();
        mHandler = new Handler();
        setbyme = 0;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FirebaseDatabase.getInstance().goOnline();
        notdouble = notdouble2 = 0;
        pre = PreferenceManager.getDefaultSharedPreferences(this);
        getEvery = getIntent();
        destinationuid = getEvery.getStringExtra("destinationUid");
        gameRoomuid = getEvery.getStringExtra("gameroomUid");
        startitem = getEvery.getIntExtra("startItem", -1);
        gameitem = getEvery.getIntExtra("gameItem", -1);
        vibe = getEvery.getIntExtra("vibe", 1);
        effect_volume = pre.getFloat("effectvolume", (float) 0.5);

        chatModel = new RoomModel();
        whos = 0;
        itemnum = 0;
        //chatModel.users.put(uid,true);
        //chatModel.users.put(destinationuid,true);
        //chatModel.turn.put(uid,true);
        //chatModel.usedCards.put(Integer.toString(cardused),true);

        handcardsArrayList = new ArrayList<>();
        roomModel = new RoomModel();

        //FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).removeValue();

        // u = java.util.UUID.fromString(GetDevicesUUID(PlayActivity.this));//Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        stacking = (TextView) findViewById(R.id.stacking);
        read = getString(R.string.stackcard);
        turnname = (TextView) findViewById(R.id.turnname);
        turntime = getString(R.string.turnname);
        carddeck = (RecyclerView) findViewById(R.id.carddeck);
        playerbg[0] = (LinearLayout) findViewById(R.id.player1);//상대방 카드 개수 보기
        playercards[0] = (TextView) findViewById(R.id.cardnum1);
        card = (ImageButton) findViewById(R.id.card);//카드내기 버튼
        draw = (ImageButton) findViewById(R.id.cardraw);//카드 먹기 버튼
        startitembtn = (ImageButton) findViewById(R.id.activity_play_startitem);
        gameitembtn = (ImageButton) findViewById(R.id.activity_play_gameitem);
        cardAdap = new RecyclerImageTextAdapter(cardlist);//카드모음 어뎁터
        mlay = new LinearLayoutManager(this);
        mlay.setOrientation(LinearLayoutManager.HORIZONTAL);
        carddeck.setLayoutManager(mlay);
        carddeck.setAdapter(cardAdap);
        //re=new send();
        String text = String.format(read, (stack + 1));
        stacking.setText(text);
        cardplace.setVolume(effect_volume, effect_volume);
        cardshuffle.setVolume(effect_volume, effect_volume);
        cardslide.setVolume(effect_volume, effect_volume);

        if (startitem != -1) {
            startitembtn.setImageDrawable(getDrawable(startitems[startitem]));
        }
        if (gameitem != -1) {
            gameitembtn.setImageDrawable(getDrawable(gameitems[gameitem]));
            gameitembtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (turn.equals(uid)) {
                        gameitembtn.setClickable(false);
                        if (gameitem == 0) {
                            if (stack == 0) {
                                stack = 2;
                            } else
                                stack += 1;
                            setbyme = 1;
                        } else if (gameitem == 1) {
                            if (stack > 0) {
                                stack -= 5;
                                if (stack < 0) {
                                    stack = 0;
                                }
                                String text;
                                text = String.format(read, stack);
                                stacking.setText(text);
                            } else if (stack == 0) {
                                Toast.makeText(getApplicationContext(), "현재는 PENALTY가 0이네요", Toast.LENGTH_SHORT).show();
                            }
                        } else if (gameitem == 2) {
                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    handcardsArrayList.clear();
                                    destiniArrayList.clear();
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        if (data.getValue() != null)
                                            handcardsArrayList.add(data.getValue(RoomModel.handcards.class));
                                        else {
                                            Toast.makeText(getApplicationContext(), "yellowcards 오류", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    for (int i = 0; i < handcardsArrayList.size(); i++) {
                                        if (destinationuid.equals(handcardsArrayList.get(i).uid)) {
                                            destiniArrayList.add(handcardsArrayList.get(i));
                                        }
                                    }
                                    Random random = new Random();
                                    String glencecard = destiniArrayList.get(random.nextInt(destiniArrayList.size())).yellowcard;
                                    View glence = getLayoutInflater().inflate(R.layout.recycler_item, null);
                                    AlertDialog.Builder glenceBuilder = new AlertDialog.Builder(PlayActivity.this);
                                    final ImageView glenceImage = (ImageView) glence.findViewById(R.id.rc_icon);
                                    glenceImage.setImageDrawable(getDrawable(cards[Integer.parseInt(glencecard)]));
                                    glenceBuilder.setView(glence);
                                    glenceDialog = glenceBuilder.create();
                                    glenceDialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            glenceDialog.dismiss();
                                        }
                                    }, 2500);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "이미 아이템을 사용하셨습니다", Toast.LENGTH_SHORT).show();
                        }
                        gameitembtn.setImageDrawable(getDrawable(gameoffitems[gameitem]));
                        gameitem = -1;
                    }
                }
            });
        }
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("winner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null && gamestart != 0) {
                    String winner = (String) snapshot.getValue();
                    View versus = getLayoutInflater().inflate(R.layout.activity_winner, null);
                    AlertDialog.Builder versusbuilding = new AlertDialog.Builder(PlayActivity.this);
                    versusbuilding.setView(versus);
                    final ImageView resultImg = (ImageView) versus.findViewById(R.id.winner_img);
                    if (winner.equals(uid)) {
                        resultImg.setImageDrawable(getDrawable(R.drawable.play_win2));
                        resultDialog = versusbuilding.create();
                        if (resultDialog != null)
                            resultDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resultDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                gamestart = 0;
                                startActivity(intent);
                                //FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).setValue(null);
                                if (Build.VERSION.SDK_INT >= 21) {
                                    finishAndRemoveTask();
                                } else {
                                    finish();
                                }
                            }
                        }, 4000);
                        Toast.makeText(getApplicationContext(), "승리 하였습니다", Toast.LENGTH_SHORT).show();
                    } else if (winner.equals(destinationuid)) {
                        resultImg.setImageDrawable(getDrawable(R.drawable.play_lose2));
                        resultDialog = versusbuilding.create();
                        resultDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resultDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                gamestart = 0;
                                startActivity(intent);
                                //FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).setValue(null);
                                if (Build.VERSION.SDK_INT >= 21) {
                                    finishAndRemoveTask();
                                } else {
                                    finish();
                                }
                            }
                        }, 4000);
                        Toast.makeText(getApplicationContext(), "패배 하였습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // addItem(getDrawable(cards[]), );
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (whos != 1) {
                        String s = (String) snapshot.getValue();
                        itemnum = Integer.parseInt(s);

                        if (itemnum == 1 && turn.equals(uid) && whos != 1) {
                            Toast.makeText(getApplicationContext(), "상대방이 전체 카드 바꾸기 아이템 사용!", Toast.LENGTH_SHORT).show();
                            if (gameitem != 1) {
                                restart();
                            } else {
                                selected = 1;
                                AlertDialog.Builder abc = new AlertDialog.Builder(PlayActivity.this);
                                abc.setMessage("방어권을 사용하시겠습니까?");
                                abc.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selected = 0;
                                        dialog.dismiss();
                                        gameitembtn.setImageDrawable(getDrawable(gameoffitems[gameitem]));
                                        gameitem = -1;
                                        turnstage();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                            }
                                        }, 1000);
                                    }
                                });
                                abc.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selected = 0;
                                        dialog.dismiss();
                                        restart();

                                    }
                                });
                                abcd = abc.create();
                                abcd.show();
                                abcd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        if (selected == 1) {
                                            abcd.show();
                                        }
                                    }
                                });
                            }
                        } else if (itemnum == 2 && !turn.equals(uid) && whos != 1) {
                            Toast.makeText(getApplicationContext(), "상대방이 내 카드 바꾸기 아이템 사용!", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        } else if (itemnum == 3 && !turn.equals(uid) && whos != 1) {
                            Toast.makeText(getApplicationContext(), "상대방이 두배 뽑기 아이템 사용!", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                        } else if (itemnum == 4 && !turn.equals(uid) && whos != 1) {
                            Toast.makeText(getApplicationContext(), "상대방이 방어권 아이템 사용!", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                        } else if (itemnum == 5 && !turn.equals(uid) && whos != 1) {
                            Toast.makeText(getApplicationContext(), "상대방이 상대방에게 패널티 추가 아이템 사용!", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                        } else if (itemnum == 6 && !turn.equals(uid) && whos != 1) {
                            Toast.makeText(getApplicationContext(), "상대방이 상대방 카드 훔쳐보기 아이템 사용!", Toast.LENGTH_LONG).show();

                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("gamestart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null && gamestart == 1) {
                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("gamestart").setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        somebodyArrayList.clear();
                        for(DataSnapshot d:snapshot.getChildren()){
                            if(d.getValue()!=null)
                            somebodyArrayList.add(d.getValue(RoomModel.somebody.class));
                            else{
                                Toast.makeText(getApplicationContext(),"somebodyshands 오류",Toast.LENGTH_SHORT).show();
                            }
                        }

                        for(int i = 0; i< somebodyArrayList.size(); i++){
                            for(int g=0;g<54;g++){
                                if(i==Integer.parseInt(somebodyArrayList.get(i).handcard)){
                                    if(carduse[Integer.parseInt(somebodyArrayList.get(i).handcard)]==0)
                                        carduse[Integer.parseInt(somebodyArrayList.get(i).handcard)]=1;
                                }
                                else if(!turn.equals(uid)){
                                    if(carduse[i]==1)
                                        carduse[i]=0;
                                }
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });*/
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("usedCards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomModel.usingCards use3 = new RoomModel.usingCards();
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.getValue() != null) {
                        use3 = item.getValue(RoomModel.usingCards.class);
                        if (use3.usingCard != null) {
                            if (mstate != -1 && (mstate + 1) % 13 != 7)//처음 카드를 낼때와 7을 냈을 경우에는 제외함
                                if (carduse[mstate] == 1) {
                                    carduse[mstate] = 0;
                                }
                            mstate = Integer.parseInt(use3.usingCard);
                            carduse[mstate] = 1;
                            card.setImageDrawable(getDrawable(cards[mstate]));
                            cardplace.start();
                        } else if (use3.stack != null) {
                            String text;
                            stack = Integer.parseInt(use3.stack);
                            if (stack != 0)
                                text = String.format(read, stack);
                            else
                                text = String.format(read, stack + 1);
                            stacking.setText(text);
                        }
                        if (use3.mark != null) {
                            carduse[mstate] = 0;
                            mstate = (Integer.parseInt(use3.mark) - 1) * 13 + 6;
                            card.setImageDrawable(getDrawable(cards[mstate]));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "usedcards 오류", Toast.LENGTH_SHORT).show();
                    }
                }

                /* if(mstate!=-1)
                if(carduse[mstate]==1){
                    carduse[mstate]=0;
                }
                for(DataSnapshot item:snapshot.getChildren()){
                    mstate=Integer.parseInt(item.getKey());
                }
                carduse[mstate]=1;
                card.setBackground(getDrawable(cards[mstate]));*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carddeck.scrollTo(0, 0);
                carddeck.setClickable(false);
                int destinicards = 0;
                if (po < cardlist.size() && cardlist.size() > 0 && po > -1)
                    removeItem(po);
                po = -1;
                cardlist.clear();

                handcardsArrayList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getValue() != null)
                        handcardsArrayList.add(data.getValue(RoomModel.handcards.class));
                    else {
                        Toast.makeText(getApplicationContext(), "yellowcards 오류", Toast.LENGTH_SHORT).show();
                    }
                }
                for (int i = 0; i < 54; i++) {
                    if (i != mstate) {
                        if (carduse[i] == 1) carduse[i] = 0;
                    }
                }
                for (int i = 0; i < handcardsArrayList.size(); i++) {
                    if (carduse[Integer.parseInt(handcardsArrayList.get(i).yellowcard)] == 0)
                        carduse[Integer.parseInt(handcardsArrayList.get(i).yellowcard)] = 1;
                    if (uid.equals(handcardsArrayList.get(i).uid)) {
                        addItem(getDrawable(cards[Integer.parseInt(handcardsArrayList.get(i).yellowcard)]), handcardsArrayList.get(i).yellowcard);
                        if (cardfinish == 0) {
                            cardfinish = 1;
                        }
                    } else if (destinationuid.equals(handcardsArrayList.get(i).uid)) {
                        destinicards++;
                        playerbg[0].setVisibility(View.VISIBLE);
                    }
                    cardAdap.notifyDataSetChanged();
                    playercards[0].setText(Integer.toString(destinicards));
                    if (i == handcardsArrayList.size() - 1) {
                        carddeck.setClickable(true);
                        /*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(cardlist.size()==0&&cardfinish!=0&&whos==0&&itemnum==0){
                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("winner").setValue(uid);
                                }
                            }
                        },3000);*/
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("turn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.getKey() == null) {
                        Toast.makeText(getApplicationContext(), "turn 오류", Toast.LENGTH_SHORT).show();
                    } else {
                        turn = item.getKey();
                        if (turn.equals(uid) && cardfinish == 0) {
                            cardshuffle.start();
                            randcard(5);
                            View tutorialView = getLayoutInflater().inflate(R.layout.recycler_item, null);
                            AlertDialog.Builder tutorialBuilder = new AlertDialog.Builder(PlayActivity.this);
                            tutorialBuilder.setView(tutorialView);
                            final ImageView tutorialImage = (ImageView) tutorialView.findViewById(R.id.rc_icon);
                            tutorialImage.setImageDrawable(getDrawable(R.drawable.play_explain));
                            tutorial = tutorialBuilder.create();
                            tutorial.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tutorial.dismiss();
                                }
                            }, 3000);
                            tutorial.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    turnstage();
                                    cardfinish = 1;
                                }
                            });
                        } else if (turn.equals(uid) && cardfinish == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (turn.equals(uid) && startitem != -1 && itemnum == 0) {
                                        selected = 1;
                                        AlertDialog.Builder ab = new AlertDialog.Builder(PlayActivity.this);
                                        if (startitem == 0) {
                                            ab.setMessage("내 카드를 전부 다시 뽑기 아이템을 사용하시겠습니까?");
                                        } else if (startitem == 1) {
                                            ab.setMessage("전체 플레이어 카드 다시 뽑기 아이템을 사용하시겠습니까?");
                                        } else if (startitem == 2) {
                                            ab.setMessage("카드 두배 뽑기 아이템을 사용하시겠습니까?");
                                        }

                                        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                selected = 0;
                                                whos = 1;
                                                if (startitem == 0) {
                                                    for (int i = 0; i < cardlist.size(); i++) {
                                                        String cardnum = cardlist.get(i).getCardno();
                                                        carduse[Integer.parseInt(cardnum)] = 0;
                                                        if (i == cardlist.size() - 1) {
                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {//저장되어 있는 누군가의 손에 있는 카드를 삭제
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    for (int i = 0; i < 54; i++) {
                                                                        if (carduse[i] == 1) {
                                                                            RoomModel.somebody roomModelso = new RoomModel.somebody();
                                                                            roomModelso.handcard = Integer.toString(i);
                                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").push().setValue(roomModelso).addOnCompleteListener(new OnCompleteListener<Void>() {//다시 저장
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                }
                                                                            });
                                                                        }
                                                                        if (i == 53) {
                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    recard();
                                                                                }
                                                                            }, 2000);

                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    randcard(5);
                                                                                }
                                                                            }, 3000);
                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("2");
                                                                                }
                                                                            }, 4000);
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                       /* new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                alreadyused();
                                                            }
                                                        },1000);*/


                                                } else if (startitem == 1) {

                                                    for (int i = 0; i < cardlist.size(); i++) {
                                                        String cardnum = cardlist.get(i).getCardno();
                                                        carduse[Integer.parseInt(cardnum)] = 0;
                                                        if (i == cardlist.size() - 1) {
                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {//저장되어 있는 누군가의 손에 있는 카드를 삭제
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    for (int i = 0; i < 54; i++) {
                                                                        if (carduse[i] == 1) {
                                                                            RoomModel.somebody roomModelso = new RoomModel.somebody();
                                                                            roomModelso.handcard = Integer.toString(i);
                                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").push().setValue(roomModelso).addOnCompleteListener(new OnCompleteListener<Void>() {//다시 저장
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                }
                                                                            });
                                                                        }
                                                                        if (i == 53) {
                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    recard();
                                                                                }
                                                                            }, 1000);

                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    randcard(5);
                                                                                }
                                                                            }, 2000);

                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    turnstage();
                                                                                }
                                                                            }, 3000);
                                                                            new Handler().postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("1");
                                                                                }
                                                                            }, 4000);
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }


                                                } else if (startitem == 2) {
                                                    randcard(5);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("3");
                                                        }
                                                    }, 1000);
                                                }
                                                startitembtn.setImageDrawable(getDrawable(startoffitems[startitem]));
                                                startitem = -1;
                                                dialog.dismiss();
                                                if (vibe == 1) {
                                                    vibrator.vibrate(500);
                                                }
                                                Toast.makeText(getApplicationContext(), "당신의 차례입니다", Toast.LENGTH_SHORT).show();

                                                card.setClickable(true);
                                                draw.setClickable(true);
                                            }
                                        });
                                        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                selected = 0;
                                                dialog.dismiss();
                                                startitem = -1;
                                                if (vibe == 1) {
                                                    vibrator.vibrate(500);
                                                }
                                                Toast.makeText(getApplicationContext(), "당신의 차례입니다", Toast.LENGTH_SHORT).show();

                                                card.setClickable(true);
                                                draw.setClickable(true);
                                            }
                                        });
                                        abd = ab.create();
                                        if (abd != null)
                                            abd.show();
                                        abd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                if (selected != 0) {
                                                    abd.show();
                                                }
                                            }
                                        });
                                        abd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                if (selected != 0) {
                                                    abd.show();
                                                }
                                            }
                                        });
                                    } else if (turn.equals(uid) && itemnum == 0) {
                                        if (vibe == 1) {
                                            vibrator.vibrate(500);
                                        }
                                        Toast.makeText(getApplicationContext(), "당신의 차례입니다", Toast.LENGTH_SHORT).show();
                                        turnname.setText(String.format(turntime, "내차례"));
                                        card.setClickable(true);
                                        draw.setClickable(true);
                                    }

                                }
                            }, 1000);
                        } else if (turn.equals(destinationuid)) {
                            Toast.makeText(getApplicationContext(), "상대방의 차례입니다", Toast.LENGTH_SHORT).show();
                            turnname.setText(String.format(turntime, "상대방차례"));
                            card.setClickable(false);
                            draw.setClickable(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw.setClickable(false);
                notdouble = System.currentTimeMillis();
                if (Math.abs(notdouble2 - notdouble) > 2) {
                    if (stack == 0) {
                        cardslide.start();
                        randcard(1);
                        turnstage();
                    } else {
                        cardslide.start();
                        randcard(stack);
                        stack = 0;
                        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("usedCards").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        RoomModel romModel = new RoomModel();
                                        if (snapshot.getValue() != null) {
                                            romModel = snapshot.getValue(RoomModel.class);
                                            RoomModel.usingCards use = new RoomModel.usingCards();
                                            RoomModel.usingCards use2 = new RoomModel.usingCards();
                                            use.usingCard = Integer.toString(mstate);
                                            romModel.usedCards.put("usingCards", use);
                                            use2.stack = Integer.toString(stack);
                                            romModel.usedCards.put("stacks", use2);
                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).setValue(romModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("gamestart").setValue("1");
                                                    turnstage();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "draw 오류", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                } else {
                    draw.setClickable(true);
                }
            }
        });
        cardAdap.setOnItemClickListener(new RecyclerImageTextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_SHORT).show();
                if (seleccheck != 1) {
                    if (cardlist.size() > 0)
                        replaceItem(pos);
                    cardAdap.notifyDataSetChanged();
                    seleccheck = 1;
                    RecyclerItem item = cardlist.get(pos);
                    cardcheck = Integer.parseInt(item.getCardno());
                    po = pos;
                } else {
                    if (po < cardlist.size() && cardlist.size() > 0 && po > -1)
                        removeItem(po);
                    po = -1;
                    cardAdap.notifyDataSetChanged();
                    seleccheck = 0;
                    cardcheck = -1;
                }
            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notdouble2 = System.currentTimeMillis();
                if (Math.abs(notdouble2 - notdouble) > 2) {
                    if (po < cardlist.size() && cardlist.size() > 0 && po > -1)
                        removeItem(po);
                    po = -1;
                    cardAdap.notifyDataSetChanged();
                    //sendData(Integer.toString(cardcheck));
                    if (cardcheck >= 0 && cardcheck <= 12) {
                        if ((mstate >= 13 && mstate <= 51) || (stack != 0 && setbyme != 1)) {
                            seleccheck = -1;
                        }
                        if (mstate == 1 && cardcheck == 2 && seleccheck == -1) {
                            seleccheck = 1;
                        }
                        if (mstate == 1 && cardcheck == 0 && seleccheck == -1) {
                            seleccheck = 1;
                        }

                    } else if (cardcheck >= 13 && cardcheck <= 25) {
                        if ((mstate <= 12 || (mstate >= 26 && mstate <= 51)) || (stack != 0 && setbyme != 1)) {
                            seleccheck = -1;
                        }
                        if (mstate == 14 && cardcheck == 15 && seleccheck == -1) {
                            seleccheck = 1;
                        }
                        if (mstate == 14 && cardcheck == 13 && seleccheck == -1) {
                            seleccheck = 1;
                        }
                    } else if (cardcheck >= 26 && cardcheck <= 38) {
                        if ((mstate <= 25 || (mstate >= 39 && mstate <= 51)) || (stack != 0 && setbyme != 1)) {
                            seleccheck = -1;
                        }
                        if (mstate == 27 && cardcheck == 28 && seleccheck == -1) {
                            seleccheck = 1;
                        }
                        if (mstate == 27 && cardcheck == 26 && seleccheck == -1) {
                            seleccheck = 1;
                        }

                    } else if (cardcheck >= 39 && cardcheck <= 51) {
                        if ((mstate <= 38) || (stack != 0 && setbyme != 1)) {
                            seleccheck = -1;
                        }
                        if (mstate == 40 && cardcheck == 41 && seleccheck == -1) {
                            seleccheck = 1;
                        }
                        if (mstate == 40 && cardcheck == 39 && seleccheck == -1) {
                            seleccheck = 1;
                        }
                    }
                    if (((mstate + 1) % 13 == (cardcheck + 1) % 13) && cardcheck != 52 && cardcheck != 53 && mstate != 52 && mstate != 53) {
                        if (seleccheck == -1) {
                            seleccheck = 1;
                        }
                    }
                    if (mstate == 52) {//놓여진 카드가 52일 경우
                        if ((((cardcheck >= 0 && cardcheck <= 12) || (cardcheck >= 39 && cardcheck <= 51)) && stack == 0) || cardcheck == 0 || cardcheck == 53) {//흑조 :먹는 카드가 없는 경우 클로버나 스페이드는 낼수 있다 먹는 카드가 있어도 스페이드 에이나 컬조
                            if (seleccheck == -1) {
                                seleccheck = 1;
                            }
                        } else {
                            if (seleccheck == 1) {
                                seleccheck = -1;
                            }
                        }
                    }
                    if (mstate == 53) {
                        if ((((cardcheck >= 13 && cardcheck <= 25) || (cardcheck >= 26 && cardcheck <= 38)) && stack == 0) || cardcheck == 52) {//컬조 : 먹는 카드가 없는 경우 하트나 다이아, 흑조는 아무 때나 낼수 있음
                            if (seleccheck == -1) {
                                seleccheck = 1;
                            }
                        } else {
                            if (seleccheck == 1) {
                                seleccheck = -1;
                            }
                        }
                    }
                    if (cardcheck == 52) {
                        if (((mstate >= 0 && mstate <= 12) || (mstate >= 39 && mstate <= 51)) || mstate == 53 || mstate == 0) {//내는카드가 흑조 : 놓여진 카드가 스페이드, 클로버,컬조,스페이드에이면 가능
                            if (seleccheck == -1) {
                                seleccheck = 1;
                            }
                        } else {
                            if (seleccheck == 1) {
                                seleccheck = -1;
                            }
                        }
                    }
                    if (cardcheck == 53) {
                        if (((mstate >= 13 && mstate <= 25) || (mstate >= 26 && mstate <= 38)) || mstate == 52) {//내는카드가 컬조 : 다이아,하트,흑조면 가능
                            if (seleccheck == -1) {
                                seleccheck = 1;
                            }
                        } else {
                            if (seleccheck == 1)
                                seleccheck = -1;
                        }
                    }
                    if ((cardcheck + 1) % 13 == 7 && seleccheck == 1) {
                        if (carduse[mstate] == 1) {
                            carduse[mstate] = 0;
                        }
                        View sevencard = getLayoutInflater().inflate(R.layout.pop_sevencard, null);
                        AlertDialog.Builder sevenbuilder = new AlertDialog.Builder(PlayActivity.this);
                        sevenbuilder.setView(sevencard);
                        seven = sevenbuilder.create();
                        seven.show();
                        carduse[cardcheck] = 0;
                        final ImageButton setspade = (ImageButton) sevencard.findViewById(R.id.sevencard_spade);
                        final ImageButton setdia = (ImageButton) sevencard.findViewById(R.id.sevencard_dia);
                        final ImageButton setheart = (ImageButton) sevencard.findViewById(R.id.sevencard_heart);
                        final ImageButton setclover = (ImageButton) sevencard.findViewById(R.id.sevencard_clover);

                        setspade.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                turnstage();
                                cardon = 1;
                                seven.dismiss();
                                /*new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        givecard();
                                    }
                                },1000);*/

                            }
                        });

                        setdia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                turnstage();
                                cardon = 2;
                                seven.dismiss();

                            }
                        });
                        setheart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                turnstage();
                                cardon = 3;
                                seven.dismiss();

                            }
                        });
                        setclover.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                turnstage();
                                cardon = 4;
                                seven.dismiss();

                            }
                        });

                        seven.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (cardon == 0) {
                                    seven.show();
                                }
                            }
                        });
                    } else if (seleccheck == 1 && (cardcheck + 1) % 13 != 7) {
                        if (cardcheck == 0 || cardcheck == 52) {
                            stack += 5;
                        } else if (cardcheck == 53) {
                            stack += 7;
                        } else if ((cardcheck + 1) % 13 == 2) {//숫자가 2일때
                            stack += 2;
                        } else if ((cardcheck + 1) % 13 == 1) {//a일때
                            stack += 3;
                        } else if ((cardcheck + 1) % 13 == 3 && stack >= 2) {
                            stack -= 2;
                        }
                        if (carduse[mstate] == 1) {
                            carduse[mstate] = 0;
                        }
                        card.setClickable(false);
                        Toast.makeText(getApplicationContext(), "서버와 통신 중입니다", Toast.LENGTH_SHORT).show();

                    /*cardlist.remove(po);
                    cardAdap.notifyDataSetChanged();*/
                        if ((cardcheck + 1) % 13 <= 10 && (cardcheck + 1) % 13 != 0 && (cardcheck + 1) % 13 != 6) {
                            turnstage();
                        } else {
                            givecard();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alreadyused();
                                }
                            }, 1500);

                            seleccheck = 0;
                            Toast.makeText(getApplicationContext(), "한 장 더 낼 수 있습니다", Toast.LENGTH_SHORT).show();
                        }


                    } else if (seleccheck == 0) {
                        Toast.makeText(getApplicationContext(), "카드를 먼저 선택하여 주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "낼 수 없는 카드입니다 다른카드를 선택하여 주세요", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


    void restart() {
        for (int i = 0; i < cardlist.size(); i++) {
            String cardnum = cardlist.get(i).getCardno();
            carduse[Integer.parseInt(cardnum)] = 0;
            if (i == cardlist.size() - 1) {
                alreadyused();
            }
        }
        int howcard = cardlist.size();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recard();
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                randcard(howcard);
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("item").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        }, 4000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                turnstage();
            }
        }, 5000);
    }

    void turnstage() {
        card.setClickable(false);
        draw.setClickable(false);
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("turn").child(uid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomModel = new RoomModel();
                        if (snapshot.getValue() != null) {
                            roomModel = snapshot.getValue(RoomModel.class);
                            RoomModel.usingCards using = new RoomModel.usingCards();
                            using.usingCard = Integer.toString(mstate);
                            RoomModel.usingCards usingCards2 = new RoomModel.usingCards();
                            usingCards2.stack = Integer.toString(stack);
                            //roomModel.usedCards.put("usingCards",using);
                            // roomModel.usedCards.put("stack",usingCards2);
                            roomModel.turn.put(destinationuid, true);
                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).setValue(roomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (seleccheck == 1) {
                                        givecard();
                                        seleccheck = 0;
                                    }
                                    if (whos != 0) {
                                        whos = 0;
                                    }
                                    if (setbyme != 0) {
                                        setbyme = 0;
                                    }

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "turnstage 오류", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {//저장되어 있는 누군가의 손에 있는 카드를 삭제
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        for (int i = 0; i < 54; i++) {
                            if (carduse[i] == 1) {
                                RoomModel.somebody roomModelso = new RoomModel.somebody();
                                roomModelso.handcard = Integer.toString(i);
                                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").push().setValue(roomModelso).addOnCompleteListener(new OnCompleteListener<Void>() {//다시 저장
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }
                    }
                });
            }
        }, 1500);
    }

    void alreadyused() {
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {//저장되어 있는 누군가의 손에 있는 카드를 삭제
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                for (int i = 0; i < 54; i++) {
                    if (carduse[i] == 1) {
                        RoomModel.somebody roomModelso = new RoomModel.somebody();
                        roomModelso.handcard = Integer.toString(i);
                        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").push().setValue(roomModelso).addOnCompleteListener(new OnCompleteListener<Void>() {//다시 저장
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                    }
                }
            }
        });
    }

    void turnturn() {
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("turn").child(uid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomModel = new RoomModel();
                        if (snapshot.getValue() != null) {
                            roomModel = snapshot.getValue(RoomModel.class);
                            RoomModel.usingCards using = new RoomModel.usingCards();
                            using.usingCard = Integer.toString(mstate);
                            RoomModel.usingCards usingCards2 = new RoomModel.usingCards();
                            usingCards2.stack = Integer.toString(stack);
                            //roomModel.usedCards.put("usingCards",using);
                            // roomModel.usedCards.put("stack",usingCards2);
                            roomModel.turn.put(destinationuid, true);
                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).setValue(roomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    card.setClickable(false);
                                    draw.setClickable(false);
                                    if (seleccheck == 1) {
                                        givecard();
                                        seleccheck = 0;
                                    }

                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "turnturn 오류", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    public void playeropen(int playernum) {
        playerbg[playernum].setVisibility(View.VISIBLE);
    }

    public void randcard(int how) {//how는 카드를 몇장 드로우 할 것인지 보냄
        Random r = new Random();
        RoomModel chatmodel2 = new RoomModel();

        for (int i = 0; i < how; i++) {
            ramdomNum = r.nextInt(54);
            if (carduse[ramdomNum] == 0) {
                //cardcheck=(ramdomNum*-1)-10;//뽑혀져 있는 카드는 -10부터 -61로 표기해서 보냄
                //addItem(getDrawable(cards[ramdomNum]),Integer.toString(ramdomNum));
                carduse[ramdomNum] = 1;//카드가 이미 사용중인지 아닌 지 체크 함
                RoomModel.somebody roomModel12 = new RoomModel.somebody();
                RoomModel.handcards chat = new RoomModel.handcards();
                roomModel12.handcard = Integer.toString(ramdomNum);
                chat.uid = uid;
                chat.yellowcard = Integer.toString(ramdomNum);
                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("somebodyshands").push().setValue(roomModel12);
                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").push().setValue(chat);
                //정보를 보내야함
                //cardAdap.notifyDataSetChanged();
            } else {
                i--;
            }
        }
    }

    void givecard() {
        if (po < cardlist.size() && cardlist.size() > 0 && po > -1)
            removeItem(po);
        po = -1;
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("usedCards").setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomModel34 = new RoomModel();
                        if (snapshot.getValue() != null) {
                            roomModel34 = snapshot.getValue(RoomModel.class);
                            RoomModel.usingCards use2 = new RoomModel.usingCards();
                            use2.stack = Integer.toString(stack);
                            roomModel34.usedCards.put("stacks", use2);
                            if (cardon != 0) {
                                carduse[cardcheck] = 0;
                                RoomModel.usingCards use4 = new RoomModel.usingCards();
                                use4.mark = Integer.toString(cardon);
                                roomModel34.usedCards.put("mark", use4);
                                cardon = 0;
                            } else {
                                RoomModel.usingCards use = new RoomModel.usingCards();
                                use.usingCard = Integer.toString(cardcheck);
                                roomModel34.usedCards.put("usingCards", use);
                            }

                            //roomModel34.usedCards.put(Integer.toString(cardcheck),true);
                            if (PlayActivity.this.roomModel.usedCards != null) {
                                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).setValue(roomModel34).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                handcardsArrayList.clear();
                                                mycardArrayList.clear();
                                                destiniArrayList.clear();
                                                some.clear();
                                                for (DataSnapshot data : snapshot.getChildren()) {
                                                    if (data.getValue() != null) {
                                                        handcardsArrayList.add(data.getValue(RoomModel.handcards.class));
                                                        if (data.getValue(RoomModel.handcards.class).yellowcard.equals(Integer.toString(cardcheck))) {
                                                            removecard = data.getKey();
                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "givecard yellowcards 오류", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                for (int i = 0; i < handcardsArrayList.size(); i++) {
                                                    if (uid.equals(handcardsArrayList.get(i).uid)) {
                                                        mycardArrayList.add(handcardsArrayList.get(i));
                                                    } else if (destinationuid.equals(handcardsArrayList.get(i).uid)) {
                                                        destiniArrayList.add(handcardsArrayList.get(i));
                                                    }
                                                }
                                                for (int i = 0; i < destiniArrayList.size(); i++) {
                                                    some.add(destiniArrayList.get(i));
                                                }
                                                for (int i = 0; i < mycardArrayList.size(); i++) {
                                                    if (Integer.parseInt(mycardArrayList.get(i).yellowcard) != cardcheck) {
                                                        some.add(mycardArrayList.get(i));
                                                    } else {

                                                        if (mycardArrayList.size() == 1 && itemnum == 0 && whos == 0) {
                                                            cardlist.clear();
                                                            cardAdap.notifyDataSetChanged();
                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("winner").setValue(uid);
                                                            Toast.makeText(getApplicationContext(), "승리하셨습니다", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }
                                                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        for (int i = 0; i < some.size(); i++) {
                                                            chat2 = new RoomModel.handcards();
                                                            chat2.uid = some.get(i).uid;
                                                            chat2.yellowcard = some.get(i).yellowcard;

                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").push().setValue(chat2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    card.setClickable(true);

                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "givecard usedcard 오류", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    void recard() {
        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (po < cardlist.size() && cardlist.size() > 0 && po > -1)
                    removeItem(po);
                po = -1;
                handcardsArrayList.clear();
                destiniArrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getValue() != null) {
                        handcardsArrayList.add(data.getValue(RoomModel.handcards.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "givecard yellowcards 오류", Toast.LENGTH_SHORT).show();
                    }
                }
                for (int i = 0; i < handcardsArrayList.size(); i++) {
                    if (destinationuid.equals(handcardsArrayList.get(i).uid)) {
                        destiniArrayList.add(handcardsArrayList.get(i));
                    }
                }

                ArrayList<RoomModel.handcards> some = new ArrayList<>();
                for (int i = 0; i < destiniArrayList.size(); i++) {
                    some.add(destiniArrayList.get(i));
                }

                FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        for (int i = 0; i < some.size(); i++) {
                            RoomModel.handcards chat2 = new RoomModel.handcards();
                            chat2.uid = some.get(i).uid;
                            chat2.yellowcard = some.get(i).yellowcard;

                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameRoomuid).child("yellowcards").push().setValue(chat2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    card.setClickable(true);
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public void addItem(Drawable icon, String tx) {
        RecyclerItem item = new RecyclerItem();
        item.setIcon(icon);
        item.setCard(tx);
        cardlist.add(item);
    }

    public void replaceItem(int index) {
        RecyclerItem item = cardlist.get(index);
        item.setSelected(getDrawable(R.drawable.card_selected));
    }

    public void removeItem(int index) {
        RecyclerItem item = cardlist.get(index);
        item.setSelected(null);
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
                if (Build.VERSION.SDK_INT >= 21) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        }
    }

}





