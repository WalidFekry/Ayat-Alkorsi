package com.walid.ayatalkorsi;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class MediaPlayerActivity extends AppCompatActivity {

    private Button playButton, pauseButton, stopButton;
    private MediaPlayer mediaPlayer;
    private View playAnim, pauseAnim, stopAnim;
    private Spinner repeatSpinner;
    private AppCompatTextView toolbarTitle;
    private AppCompatImageView reciterImage;
    private int mediaPlayerResId;

    private int repeatCount = -1;
    private int currentRepeat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        repeatSpinner = findViewById(R.id.repeat_spinner);
        playButton = findViewById(R.id.playid);
        pauseButton = findViewById(R.id.pauseid);
        stopButton = findViewById(R.id.stopid);
        playAnim = findViewById(R.id.playlottie);
        pauseAnim = findViewById(R.id.pauselottie);
        stopAnim = findViewById(R.id.stoplottie);
        toolbarTitle = findViewById(R.id.toolbar_title);
        reciterImage = findViewById(R.id.reciter_image);

        getData();

        setupSpinner();

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            stopMedia();
            finish();
        });

        playButton.setOnClickListener(v -> playMedia());
        pauseButton.setOnClickListener(v -> pauseMedia());
        stopButton.setOnClickListener(v -> stopMedia());

        updateUI(false, false);
    }

    private void getData() {
        String reciterName = getIntent().getStringExtra("TITLE");
        int reciterImageResId = getIntent().getIntExtra("RECITER_IMAGE", R.drawable.islam);
        int mediaPlayer = getIntent().getIntExtra("MEDIA_PLAYER", R.raw.islam);
        toolbarTitle.setText(reciterName);
        reciterImage.setImageResource(reciterImageResId);
        mediaPlayerResId = mediaPlayer;
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeat_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                switch (selected) {
                    case "1 مرة":
                        repeatCount = 1;
                        break;
                    case "3 مرات":
                        repeatCount = 3;
                        break;
                    case "4 مرات":
                        repeatCount = 4;
                        break;
                    case "5 مرات":
                        repeatCount = 5;
                        break;
                    case "7 مرات":
                        repeatCount = 7;
                        break;
                    case "10 مرات":
                        repeatCount = 10;
                        break;
                    case "15 مرة":
                        repeatCount = 15;
                        break;
                    case "20 مرة":
                        repeatCount = 20;
                        break;
                    case "30 مرة":
                        repeatCount = 30;
                        break;
                    case "50 مرة":
                        repeatCount = 50;
                        break;
                    case "100 مرة":
                        repeatCount = 100;
                        break;
                    case "تكرار دائم":
                        repeatCount = -1;
                        break;
                }

                Toast.makeText(MediaPlayerActivity.this, "عدد التكرارات: " + selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                repeatCount = -1;
            }
        });
    }

    private void playMedia() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, mediaPlayerResId);
            mediaPlayer.setOnCompletionListener(mp -> {
                if (repeatCount == -1 || ++currentRepeat < repeatCount) {
                    mediaPlayer.start();
                } else {
                    updateUI(false, false);
                }
            });
        }
        currentRepeat = 0;
        mediaPlayer.start();
        updateUI(true, false);
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
            mediaPlayer = null;
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
