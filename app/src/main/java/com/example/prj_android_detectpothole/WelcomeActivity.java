package com.example.prj_android_detectpothole;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    Button button_login;
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


        // Navigate to SignUpActivity
        try{
            button_login = findViewById(R.id.button_login);
            button_login.setOnClickListener(new View.OnClickListener() {
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
        catch (Exception exception){

        }

    }
    private boolean isTokenValid() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = sharedPreferences.getString("accessToken", null);
        long expireTime = sharedPreferences.getLong("expireTime", 0);

        return accessToken != null && System.currentTimeMillis() < expireTime;
    }
}