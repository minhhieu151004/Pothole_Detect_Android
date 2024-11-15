package com.example.prj_android_detectpothole;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTokenValid())
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            setContentView(R.layout.welcome_screen);
        }

        // Navigate to LoginActivity
        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login button clicked");
                try {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                } catch (Exception e) {
                    Log.e(TAG, "Error starting LoginActivity: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        // Navigate to SignUpActivity
        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
            }
        });
        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, ForgotPassword1Activity.class));
            }
        });
    }
    private boolean isTokenValid() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = sharedPreferences.getString("accessToken", null);
        long expireTime = sharedPreferences.getLong("expireTime", 0);

        return accessToken != null && System.currentTimeMillis() < expireTime;
    }
}