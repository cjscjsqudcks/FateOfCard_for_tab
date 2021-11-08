package com.example.fateofcard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fateofcard.model.RoomModel;
import com.example.fateofcard.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserActivity extends AppCompatActivity {
    RecyclerView r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);
        r = (RecyclerView) findViewById(R.id.fragment_user_RecyclerView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//전체화면으로 만들기
        r.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        r.setAdapter(new userRecyclerViewAdapter());
    }

    class userRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<UserModel> users;
        String uid, gameroomUid, destinationuid;

        public userRecyclerViewAdapter() {
            users = new ArrayList<>();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        UserModel userModel = d.getValue(UserModel.class);
                        if (userModel.uid.equals(uid)) {
                            continue;
                        }
                        users.add(userModel);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String s = String.format(getString(R.string.nickname), users.get(position).username);
            ((CustomViewHolder) holder).textView.setText(s);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RoomModel chatModel = new RoomModel();
                    Intent intent = new Intent(UserActivity.this, ItemActivity.class);
                    destinationuid = users.get(position).uid;
                    chatModel.users.put(uid, true);
                    chatModel.users.put(destinationuid, true);
                    chatModel.turn.put(uid, true);
                    RoomModel.usingCards use = new RoomModel.usingCards();
                    RoomModel.usingCards use2 = new RoomModel.usingCards();
                    Random r = new Random();
                    use.usingCard = Integer.toString(r.nextInt(54));


//chatModel.usedCards.put(Integer.toString(RoomTextAdapter.nextInt(54)),true);
                    FirebaseDatabase.getInstance().getReference().child("gamerooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                RoomModel roomModel1 = item.getValue(RoomModel.class);

                                if (roomModel1.users.containsKey(destinationuid)) {
                                    gameroomUid = item.getKey();
                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameroomUid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").push().setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot item : snapshot.getChildren()) {
                                                                RoomModel roomModel1 = item.getValue(RoomModel.class);

                                                                if (roomModel1.users.containsKey(destinationuid)) {
                                                                    roomModel1.usedCards.put("usingCards", use);
                                                                    use2.stack = "0";
                                                                    roomModel1.usedCards.put("stacks", use2);
                                                                    gameroomUid = item.getKey();
                                                                    FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameroomUid).setValue(roomModel1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameroomUid).child("gamestart").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    intent.putExtra("type", 0);//유저가 방장인지 아닌지 알기 위해
                                                                                    intent.putExtra("destinationUid", destinationuid);
                                                                                    intent.putExtra("gameroomUid", gameroomUid);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                }
                                                                            });

                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                }
                                            });
                                        }
                                    });
                                }

                            }
                            if (gameroomUid == null) {
                                FirebaseDatabase.getInstance().getReference().child("gamerooms").push().setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference().child("gamerooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot item : snapshot.getChildren()) {
                                                    RoomModel roomModel1 = item.getValue(RoomModel.class);

                                                    if (roomModel1.users.containsKey(destinationuid)) {
                                                        roomModel1.usedCards.put("usingCards", use);
                                                        use2.stack = "0";
                                                        roomModel1.usedCards.put("stacks", use2);
                                                        gameroomUid = item.getKey();
                                                        FirebaseDatabase.getInstance().getReference().child("gamerooms").child(gameroomUid).setValue(roomModel1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                intent.putExtra("type", 0);
                                                                intent.putExtra("destinationUid", destinationuid);
                                                                intent.putExtra("gameroomUid", gameroomUid);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });


        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.user_item_textview);
            }
        }
    }
}
