package com.walid.ayatalkorsi;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class MainActivity11 extends AppCompatActivity {

    private Button playButton, pauseButton, stopButton;
    private MediaPlayer mediaPlayer;
    private View playAnim, pauseAnim, stopAnim;
    private Switch loopingSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        loopingSwitch = findViewById(R.id.looping);
        playButton = findViewById(R.id.playid);
        pauseButton = findViewById(R.id.pauseid);
        stopButton = findViewById(R.id.stopid);
        playAnim = findViewById(R.id.playlottie);
        pauseAnim = findViewById(R.id.pauselottie);
        stopAnim = findViewById(R.id.stoplottie);

        mediaPlayer = MediaPlayer.create(this, R.raw.rashed);
        mediaPlayer.setLooping(true);
        loopingSwitch.setChecked(true);

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            stopMedia();
            finish();
        });


        loopingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mediaPlayer.setLooping(isChecked);
            Toast.makeText(this, isChecked ? "تم تفعيل التكرار" : "تم إيقاف التكرار", Toast.LENGTH_SHORT).show();
        });


        playButton.setOnClickListener(v -> playMedia());
        pauseButton.setOnClickListener(v -> pauseMedia());
        stopButton.setOnClickListener(v -> stopMedia());

        updateUI(false, false);
    }

    private void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateUI(true, false);
        }
    }

    private void pauseMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updateUI(false, true);
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, R.raw.rashed);
            mediaPlayer.setLooping(loopingSwitch.isChecked());
            updateUI(false, false);
        }
    }

    private void updateUI(boolean isPlaying, boolean isPaused) {
        playButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        pauseButton.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
        stopButton.setVisibility(isPlaying || isPaused ? View.VISIBLE : View.GONE);

        playAnim.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
        pauseAnim.setVisibility(isPaused ? View.VISIBLE : View.GONE);
        stopAnim.setVisibility(!isPlaying && !isPaused ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
