package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgotPassword3Activity extends AppCompatActivity {
    EditText newPassword, confirmNewPassword;
    TextView confirmError;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass3);
        ImageView btnBack = findViewById(R.id.btn_back);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_new_password);
        confirmError = findViewById(R.id.confirm_password_invalid);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Go to Login
        Button resetPass = findViewById(R.id.reset_pass);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
                    ResetPassword();
                } else {
                    confirmError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void ResetPassword() {
        String email = getIntent().getStringExtra("email");
        String pin = getIntent().getStringExtra("pin");

        ApiClient apiClient = new ApiClient();
        apiClient.resetPassword(email, pin, newPassword.getText().toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ForgotPassword3Activity.this, "Reset fail", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(ForgotPassword3Activity.this, "Pin code has been sent to email", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(ForgotPassword3Activity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    runOnUiThread(() -> Toast.makeText(ForgotPassword3Activity.this, "Login Failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}