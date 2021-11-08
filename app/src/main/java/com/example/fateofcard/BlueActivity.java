package com.example.fateofcard;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fateofcard.model.ChatModel;
import com.example.fateofcard.model.RoomModel;
import com.example.fateofcard.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BlueActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태

    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터

    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋

    Intent gameplay;

    ArrayList<RoomModel> dname;
    ArrayList<String> destinationUsers, gameroomUid;

    RecyclerView btdevice;

    String uid;

    int truely, pariedDeviceCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameplay = new Intent(getApplicationContext(), PlayActivity.class);
        dname = new ArrayList<>();
        gameroomUid = new ArrayList<>();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btdevice = (RecyclerView) findViewById(R.id.btdevice);
        btdevice.setAdapter(new RoomRecyclerViewAdapter());
        btdevice.setLayoutManager(new LinearLayoutManager(BlueActivity.this));
        destinationUsers = new ArrayList<>();


    }

    class RoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public RoomRecyclerViewAdapter() {
            FirebaseDatabase.getInstance().getReference().child("gamerooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dname.clear();
                    for (DataSnapshot item : snapshot.getChildren()) {
                        dname.add(item.getValue(RoomModel.class));
                        gameroomUid.add(item.getKey());
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            String destinatnionUid = null;
            for (String user : dname.get(position).users.keySet()) {
                if (!user.equals(uid)) {
                    destinatnionUid = user;
                    destinationUsers.add(destinatnionUid);
                }

            }
            FirebaseDatabase.getInstance().getReference().child("users").child(destinatnionUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    String name = userModel.username + "님의 방";
                    customViewHolder.textView.setText(name);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BlueActivity.this, ItemActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("destinationUid", destinationUsers.get(position));
                    intent.putExtra("gameroomUid", gameroomUid.get(position));
                    startActivity(intent);
                    finishAndRemoveTask();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dname.size();
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



