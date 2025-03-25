package com.walid.ayatalkorsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
                {R.id.yasser, MainActivity2.class},
                {R.id.khaled, MainActivity3.class},
                {R.id.fares, MainActivity4.class},
                {R.id.saad, MainActivity5.class},
                {R.id.abdo, MainActivity6.class},
                {R.id.abdo2, MainActivity7.class},
                {R.id.men, MainActivity8.class},
                {R.id.nasser, MainActivity9.class},
                {R.id.islam, MainActivity10.class},
                {R.id.rashed, MainActivity11.class}
        };

        for (Object[] mapping : cardViewMappings) {
            int cardViewId = (int) mapping[0];
            Class<?> targetActivity = (Class<?>) mapping[1];
            setupCardView(cardViewId, targetActivity);
        }
    }

    private void setupCardView(int cardViewId, Class<?> targetActivity) {
        CardView cardView = findViewById(cardViewId);
        if (cardView != null) {
            cardView.setOnClickListener(v -> startActivity(new Intent(Listen.this, targetActivity)));
        }
    }
}
