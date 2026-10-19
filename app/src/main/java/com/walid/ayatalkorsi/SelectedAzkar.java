package com.walid.ayatalkorsi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class SelectedAzkar extends AppCompatActivity {

    private static final String PREFS_NAME = "AyatAlkorsiAzkarPrefs";

    private Button btnCount1, btnCount2, btnCount3;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_azkar);

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        btnCount1 = findViewById(R.id.btn_count_zikr1);
        btnCount2 = findViewById(R.id.btn_count_zikr2);
        btnCount3 = findViewById(R.id.btn_count_zikr3);

        loadCounts();
        updateUI();

        btnCount1.setOnClickListener(v -> {
            count1 = (count1 + 1) % 2;
            saveCounts();
            updateUI();
        });

        btnCount2.setOnClickListener(v -> {
            count2 = (count2 + 1) % 2;
            saveCounts();
            updateUI();
        });

        btnCount3.setOnClickListener(v -> {
            count3 = (count3 + 1) % 4;
            saveCounts();
            updateUI();
        });
    }

    private void loadCounts() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        count1 = prefs.getInt("count1", 0);
        count2 = prefs.getInt("count2", 0);
        count3 = prefs.getInt("count3", 0);
    }

    private void saveCounts() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("count1", count1);
        editor.putInt("count2", count2);
        editor.putInt("count3", count3);
        editor.apply();
    }

    private void updateUI() {
        if (count1 >= 1) {
            btnCount1.setText("تمت القراءة ✔ (1 / 1)");
        } else {
            btnCount1.setText("قُرئت (0 / 1)");
        }

        if (count2 >= 1) {
            btnCount2.setText("تمت القراءة ✔ (1 / 1)");
        } else {
            btnCount2.setText("قُرئت (0 / 1)");
        }

        if (count3 >= 3) {
            btnCount3.setText("تمت القراءة ✔ (3 / 3)");
        } else {
            btnCount3.setText("قُرئت (" + count3 + " / 3)");
        }
    }
}
