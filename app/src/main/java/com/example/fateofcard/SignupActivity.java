package com.example.fateofcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fateofcard.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText ID, nick, pw;
    ImageButton Signup;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ID = (EditText) findViewById(R.id.SignupActivity_ID);
        nick = (EditText) findViewById(R.id.SignupActivity_NICKNAME);
        pw = (EditText) findViewById(R.id.SignupActivity_PW);
        Signup = (ImageButton) findViewById(R.id.SignupActivity_Signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//전체화면으로 만들기
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ID.getText().toString().equals("") || pw.getText().toString().equals("") || nick.getText().toString().equals("")) {
                    if (ID.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "아이디를 입력하여주세요", Toast.LENGTH_SHORT).show();
                        ID.requestFocus();
                    } else if (pw.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "비밀번호를 입력하여주세요", Toast.LENGTH_SHORT).show();
                        pw.requestFocus();
                    } else if (nick.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "닉네임을 입력하여주세요", Toast.LENGTH_SHORT).show();
                        nick.requestFocus();
                    }
                } else {
                    Signup.setClickable(false);
                    Toast.makeText(getApplicationContext(), "회원가입중", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(ID.getText().toString(), pw.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Signup.setClickable(true);
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                uid = task.getResult().getUser().getUid();
                                UserModel u = new UserModel();
                                u.username = nick.getText().toString();
                                u.uid = uid;
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                        SignupActivity.this.finish();
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }
}