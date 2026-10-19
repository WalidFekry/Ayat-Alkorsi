package com.walid.ayatalkorsi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class AyaHifz extends AppCompatActivity {

    private static final String PREFS_NAME = "AyatAlkorsiHifzPrefs";
    private static final String[] SEGMENT_TITLES = {
            "المقطع الأول (1 من 5)",
            "المقطع الثاني (2 من 5)",
            "المقطع الثالث (3 من 5)",
            "المقطع الرابع (4 من 5)",
            "المقطع الخامس (5 من 5)"
    };

    private static final String[] SEGMENT_TEXTS = {
            "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ",
            "لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ",
            "مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ",
            "وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ",
            "وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ"
    };

    private TextView overallProgressTv, segmentTitleTv, segmentContentTv;
    private ProgressBar progressBar;
    private Button btnRepeat, btnMark, btnPrev, btnNext, btnReset;

    private int currentIndex = 0;
    private final boolean[] memorized = new boolean[5];
    private final int[] repeatCounts = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aya_hifz);

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        overallProgressTv = findViewById(R.id.hifz_overall_progress_tv);
        progressBar = findViewById(R.id.hifz_progress_bar);
        segmentTitleTv = findViewById(R.id.segment_title_tv);
        segmentContentTv = findViewById(R.id.segment_content_tv);

        btnRepeat = findViewById(R.id.btn_repeat_segment);
        btnMark = findViewById(R.id.btn_mark_memorized);
        btnPrev = findViewById(R.id.btn_prev_segment);
        btnNext = findViewById(R.id.btn_next_segment);
        btnReset = findViewById(R.id.btn_reset_hifz);

        loadProgress();
        updateUI();

        btnRepeat.setOnClickListener(v -> {
            repeatCounts[currentIndex]++;
            saveProgress();
            updateUI();
        });

        btnMark.setOnClickListener(v -> {
            memorized[currentIndex] = !memorized[currentIndex];
            saveProgress();
            updateUI();
            if (memorized[currentIndex]) {
                Toast.makeText(this, "مبارك! تم حفظ " + SEGMENT_TITLES[currentIndex], Toast.LENGTH_SHORT).show();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateUI();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < 4) {
                currentIndex++;
                updateUI();
            }
        });

        btnReset.setOnClickListener(v -> {
            for (int i = 0; i < 5; i++) {
                memorized[i] = false;
                repeatCounts[i] = 0;
            }
            currentIndex = 0;
            saveProgress();
            updateUI();
            Toast.makeText(this, "تم إعادة ضبط تقدم الحفظ", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProgress() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        for (int i = 0; i < 5; i++) {
            memorized[i] = prefs.getBoolean("memorized_" + i, false);
            repeatCounts[i] = prefs.getInt("repeat_" + i, 0);
        }
    }

    private void saveProgress() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        for (int i = 0; i < 5; i++) {
            editor.putBoolean("memorized_" + i, memorized[i]);
            editor.putInt("repeat_" + i, repeatCounts[i]);
        }
        editor.apply();
    }

    private void updateUI() {
        int count = 0;
        for (boolean b : memorized) {
            if (b) count++;
        }

        overallProgressTv.setText("تقدم الحفظ: " + count + " من 5 أجزاء");
        progressBar.setProgress(count);

        segmentTitleTv.setText(SEGMENT_TITLES[currentIndex]);
        segmentContentTv.setText(SEGMENT_TEXTS[currentIndex]);

        btnRepeat.setText("تكرار المقطع (" + repeatCounts[currentIndex] + ")");
        if (memorized[currentIndex]) {
            btnMark.setText("محفوظ ✔");
        } else {
            btnMark.setText("تعليم كـ محفوظ");
        }

        btnPrev.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < 4);
    }
}
