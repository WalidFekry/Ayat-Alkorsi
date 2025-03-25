package com.walid.ayatalkorsi;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import guy4444.smartrate.SmartRate;

public class AyaFadl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aya_fadl);

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        new Handler().postDelayed(() -> {
            if (!isFinishing()) {
                SmartRate.Rate(
                        AyaFadl.this,
                        "تقييم التطبيق",
                        "تقييمك للتطبيق يساعدنا على التطوير المستمر وتقديم المزيد",
                        "تقييم الآن",
                        "حسنًا، يمكنك تقييمنا الآن على جوجل بلاي",
                        "اضغط هنا",
                        "ليس الآن",
                        "Thanks",
                        Color.parseColor("#D7723F"),
                        2
                );
            }
        }, 20000);
    }
}
