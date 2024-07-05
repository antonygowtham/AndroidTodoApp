package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 2000; // 2 seconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_splash_screen);

        ImageView splashImageView = findViewById(R.id.splashImageView);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif() // Specify that it's a GIF
                .load(R.drawable.splash) // Replace with the correct resource ID
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching
                .into(splashImageView);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity
                Intent intent = new Intent(SplashScreenActivity.this, MainTabActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY_MS);
    }
    private void setAppTheme() {
        int themeId = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt("theme", R.style.Base_Theme_ToDoList);
        setTheme(themeId);
    }
}
