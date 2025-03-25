package com.walid.ayatalkorsi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager;
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

        backButton.setOnClickListener(v -> finish());

        btnUp.setOnClickListener(v -> changeTextSize(2f));
        btnDown.setOnClickListener(v -> changeTextSize(-2f));
        btnStar.setOnClickListener(v -> openUrl());
    }

    private void changeTextSize(float sizeDelta) {
        ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, ayakortxt.getTextSize() + sizeDelta);
    }

    private void openUrl() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.walid.ayatalkorsi")));
    }
}
