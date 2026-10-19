package com.walid.ayatalkorsi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

public class AyaText extends AppCompatActivity {

    private TextView ayakortxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aya_text);

        ayakortxt = findViewById(R.id.ayakortxt);
        AppCompatButton btnUp = findViewById(R.id.btn_up);
        AppCompatButton btnDown = findViewById(R.id.btn_down);
        AppCompatButton btnStar = findViewById(R.id.btn_star);
        AppCompatImageButton backButton = findViewById(R.id.back_button);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (btnUp != null) {
            btnUp.setOnClickListener(v -> changeTextSize(2f));
        }
        if (btnDown != null) {
            btnDown.setOnClickListener(v -> changeTextSize(-2f));
        }
        if (btnStar != null) {
            btnStar.setOnClickListener(v -> openUrl());
        }
    }

    private void changeTextSize(float sizeDelta) {
        if (ayakortxt != null) {
            float currentSize = ayakortxt.getTextSize();
            float newSize = currentSize + sizeDelta;
            if (newSize >= 14f && newSize <= 100f) {
                ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
            }
        }
    }

    private void openUrl() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.walid.ayatalkorsi")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
