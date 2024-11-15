package com.example.prj_android_detectpothole;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgotPassword1Activity extends AppCompatActivity {
    EditText email;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass1);

        email = findViewById(R.id.email);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Send code
        findViewById(R.id.send_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

    }

    public void sendCode() {
        ApiClient apiClient = new ApiClient();
        apiClient.sendPinByEmail(email.getText().toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ForgotPassword1Activity.this, "Invalid email", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> Toast.makeText(ForgotPassword1Activity.this, "Pin code has been sent to email", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(ForgotPassword1Activity.this, ForgotPassword2Activity.class);
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                }
                else {
                    runOnUiThread(() -> Toast.makeText(ForgotPassword1Activity.this, "Login Failed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

}