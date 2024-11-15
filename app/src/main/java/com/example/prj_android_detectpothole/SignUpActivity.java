package com.example.prj_android_detectpothole;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {  // Phải kế thừa từ AppCompatActivity
    EditText username, email, password, confirmPassword;
    TextView confirmError;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        ImageView btnBack = findViewById(R.id.btn_back);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.mail);
        confirmPassword = findViewById(R.id.confirm_password);
        confirmError = findViewById(R.id.confirm_password_invalid);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString()))
                {
                    SignUp();
                }
                else {
                    confirmError.setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ForgotPassword1Activity.class));
            }
        });
    }

    //Function
    public void SignUp() {
        ApiClient apiClient = new ApiClient();
        apiClient.signup(username.getText().toString(), password.getText().toString(),email.getText().toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}