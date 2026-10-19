package com.walid.ayatalkorsi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

public class AyaText extends AppCompatActivity {

    private static final String PREFS_NAME = "AyatAlkorsiTextPrefs";
    private static final String KEY_TEXT_SIZE = "text_size_sp";
    private TextView ayakortxt;
    private float currentTextSizeSp = 22f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aya_text);

        ayakortxt = findViewById(R.id.ayakortxt);
        AppCompatButton btnUp = findViewById(R.id.btn_up);
        AppCompatButton btnDown = findViewById(R.id.btn_down);
        AppCompatButton btnCopy = findViewById(R.id.btn_copy_text);
        AppCompatButton btnStar = findViewById(R.id.btn_star);
        AppCompatImageButton backButton = findViewById(R.id.back_button);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentTextSizeSp = prefs.getFloat(KEY_TEXT_SIZE, 22f);
        if (ayakortxt != null) {
            ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSizeSp);
        }

        if (btnUp != null) {
            btnUp.setOnClickListener(v -> changeTextSize(2f));
        }
        if (btnDown != null) {
            btnDown.setOnClickListener(v -> changeTextSize(-2f));
        }
        if (btnCopy != null) {
            btnCopy.setOnClickListener(v -> copyTextToClipboard());
        }
        if (btnStar != null) {
            btnStar.setOnClickListener(v -> openUrl());
        }
    }

    private void changeTextSize(float sizeDeltaSp) {
        if (ayakortxt != null) {
            float newSize = currentTextSizeSp + sizeDeltaSp;
            if (newSize >= 14f && newSize <= 50f) {
                currentTextSizeSp = newSize;
                ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSizeSp);
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putFloat(KEY_TEXT_SIZE, currentTextSizeSp).apply();
            }
        }
    }

    private void copyTextToClipboard() {
        if (ayakortxt != null) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("آية الكرسي", ayakortxt.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
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
