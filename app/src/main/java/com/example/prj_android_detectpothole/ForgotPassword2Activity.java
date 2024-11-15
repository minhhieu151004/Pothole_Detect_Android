package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword2Activity extends AppCompatActivity {
    EditText pin_text;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass2);
        ImageView btnBack = findViewById(R.id.btn_back);
        pin_text = findViewById(R.id.pin_text);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getIntent().getStringExtra("email");
                String pin = pin_text.getText().toString();

                Intent intent = new Intent(ForgotPassword2Activity.this, ForgotPassword3Activity.class);
                intent.putExtra("email", email);
                intent.putExtra("pin", pin);
                startActivity(intent);
            }
        });
    }



}