package com.walid.ayatalkorsi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.walid.ayatalkorsi.R;

public class MainActivity4 extends AppCompatActivity {

    Button clk1, clk2, clk3;
    MediaPlayer mdx;
    AppCompatImageButton a;
    View play,pause,stop;
    Switch looping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        looping = findViewById(R.id.looping);
        clk1 = (Button)findViewById(R.id.playid);
        clk2 = (Button)findViewById(R.id.pauseid);
        clk3 = (Button)findViewById(R.id.stopid);
        mdx = MediaPlayer.create(MainActivity4.this,R.raw.fares);

        play = findViewById(R.id.playlottie);
        pause = findViewById(R.id.pauselottie);
        stop = findViewById(R.id.stoplottie);

        a = findViewById(R.id.back_button);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdx.stop();
                finish();
            }
        });
        mdx.setLooping(true);
        looping.setChecked(true);
        looping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mdx.setLooping(true);
                    Toast.makeText(MainActivity4.this, "تم تفعيل التكرار", Toast.LENGTH_SHORT).show();
                } else {
                    mdx.setLooping(false);
                    Toast.makeText(MainActivity4.this, "تم إيقاف التكرار", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public  void clkplay(View v)
    {mdx.start();
        clk1.setVisibility(View.GONE);
        clk2.setVisibility(View.VISIBLE);
        clk3.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
    }
    public  void clkpause(View v)
    {
        mdx.pause();
        clk2.setVisibility(View.GONE);
        clk1.setVisibility(View.VISIBLE);
        clk3.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);
        stop.setVisibility(View.GONE);

    }
    public  void clkstop(View v)
    {
        clk2.setVisibility(View.GONE);
        clk1.setVisibility(View.VISIBLE);
        clk3.setVisibility(View.GONE);
        mdx.stop();
        play.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);
        mdx = MediaPlayer.create(MainActivity4.this,R.raw.fares);
        looping.setChecked(true);
    }
}