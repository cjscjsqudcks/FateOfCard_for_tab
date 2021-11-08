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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText id, pw;
    ImageButton login, signup;
    FirebaseAuth au;
    FirebaseAuth.AuthStateListener al;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = (EditText) findViewById(R.id.LoginActivity_ID);
        pw = (EditText) findViewById(R.id.LoginActivity_PW);
        login = (ImageButton) findViewById(R.id.LoginActivity_Login);
        signup = (ImageButton) findViewById(R.id.LoginActivity_Signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//전체화면으로 만들기
        au = FirebaseAuth.getInstance();
        au.signOut();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!id.getText().toString().equals("") && !pw.getText().toString().equals("")) {
                    login.setClickable(false);
                    loginEvent();
                }
                if (id.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하여주세요", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                } else if (pw.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하여주세요", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
        al = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = au.getCurrentUser();
                if (user != null) {//로그인
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {//로그아웃

                }
            }
        };

    }

    void loginEvent() {
        Toast.makeText(getApplicationContext(), "로그인중 입니다", Toast.LENGTH_SHORT).show();
        au.signInWithEmailAndPassword(id.getText().toString(), pw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                login.setClickable(true);
                if (!task.isSuccessful()) {//로그인 실패패
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        au.addAuthStateListener(al);
    }

    @Override
    protected void onStop() {
        super.onStop();
        au.removeAuthStateListener(al);
    }
}