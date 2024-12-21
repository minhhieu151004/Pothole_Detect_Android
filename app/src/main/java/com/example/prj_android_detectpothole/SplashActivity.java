package com.example.prj_android_detectpothole;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    ImageView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        loadingView = findViewById(R.id.loading_ic);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        loadingView.startAnimation(rotateAnimation);
    }
}