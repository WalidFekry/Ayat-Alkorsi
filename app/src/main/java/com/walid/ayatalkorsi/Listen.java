package com.walid.ayatalkorsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;


public class Listen extends AppCompatActivity {

    CardView cardView1;
    AppCompatImageButton a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        a = findViewById(R.id.back_button);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardView1 = findViewById(R.id.yasser);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.khaled);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.fares);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity4.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.saad);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity5.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.abdo);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity6.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.abdo2);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity7.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.men);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity8.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.nasser);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity9.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.islam);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity10.class);
                startActivity(intent);
            }
        });

        cardView1 = findViewById(R.id.rashed);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Listen.this, MainActivity11.class);
                startActivity(intent);
            }
        });


    }
}