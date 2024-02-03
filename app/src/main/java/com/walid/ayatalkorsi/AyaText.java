package com.walid.ayatalkorsi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AyaText extends AppCompatActivity {

    TextView ayakortxt;
    Button btnup, btndown, btnstar;
    AppCompatImageButton a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aya_text);

        btnup = (Button) findViewById(R.id.btn_up);
        btndown = (Button) findViewById(R.id.btn_down);
        btnstar = (Button) findViewById(R.id.btn_star);
        ayakortxt = (TextView) findViewById(R.id.ayakortxt);


        a = findViewById(R.id.back_button);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (ayakortxt.getTextSize() + 2f));
            }
        });

        btndown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayakortxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (ayakortxt.getTextSize() - 2f));

            }
        });

        btnstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.co/2xVh8rqVLS")));

            }
        });

    }
}
