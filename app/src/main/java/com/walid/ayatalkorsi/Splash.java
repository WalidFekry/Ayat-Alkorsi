package com.walid.ayatalkorsi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        runnable = () -> {
            if (!isFinishing() && !isDestroyed()) {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}
