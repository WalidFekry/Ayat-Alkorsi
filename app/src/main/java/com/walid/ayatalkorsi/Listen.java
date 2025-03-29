package com.walid.ayatalkorsi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

public class Listen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Object[][] cardViewMappings = {
                {R.id.yasser, "ياسر الدوسري", R.drawable.yasser, R.raw.yasser},
                {R.id.khaled, "خالد الجليل", R.drawable.khaled, R.raw.khaled},
                {R.id.fares, "فارس عباد", R.drawable.fares, R.raw.fares},
                {R.id.saad, "سعد الغامدي", R.drawable.saad, R.raw.saad},
                {R.id.abdo, "عبد الرحمن مسعد", R.drawable.abdo, R.raw.abdo},
                {R.id.abdo2, "عبد الرحمن السديس", R.drawable.abdo2, R.raw.abdo2},
                {R.id.men, "محمد صديق المنشاوي", R.drawable.men, R.raw.men},
                {R.id.nasser, "ناصر القطامي", R.drawable.nasser, R.raw.nasser},
                {R.id.islam, "إسلام صبحي", R.drawable.islam, R.raw.islam},
                {R.id.rashed, "راشد العفاسي", R.drawable.rashed, R.raw.rashed}
        };

        for (Object[] mapping : cardViewMappings) {
            int cardViewId = (int) mapping[0];
            String title = (String) mapping[1];
            int reciterImage = (int) mapping[2];
            int mediaPlayer = (int) mapping[3];
            setupCardView(cardViewId, title, reciterImage, mediaPlayer);
        }
    }

    private void setupCardView(int cardViewId, String title, int reciterImage, int mediaPlayer) {
        CardView cardView = findViewById(cardViewId);
        if (cardView != null) {
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(Listen.this, MediaPlayerActivity.class);
                intent.putExtra("TITLE", title);
                intent.putExtra("RECITER_IMAGE", reciterImage);
                intent.putExtra("MEDIA_PLAYER", mediaPlayer);
                startActivity(intent);
            });
        }
    }
}