package com.walid.ayatalkorsi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class MediaPlayerActivity extends AppCompatActivity implements AudioService.AudioServiceCallback {

    private Button playButton, pauseButton, stopButton;
    private View playAnim, pauseAnim, stopAnim;
    private Spinner repeatSpinner, sleepTimerSpinner;
    private TextView repeatCounterTv;
    private AppCompatTextView toolbarTitle;
    private AppCompatImageView reciterImage;

    private String reciterName;
    private int reciterImageResId;
    private int mediaPlayerResId;

    private AudioService audioService;
    private boolean isBound = false;
    private int selectedRepeatCount = -1;
    private boolean isInitialSpinnerSelection = true;
    private boolean isInitialSleepSelection = true;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder) service;
            audioService = binder.getService();
            isBound = true;
            audioService.setCallback(MediaPlayerActivity.this);
            audioService.setMediaData(reciterName, reciterImageResId, mediaPlayerResId, selectedRepeatCount);
            updateUIFromService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            audioService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        repeatSpinner = findViewById(R.id.repeat_spinner);
        sleepTimerSpinner = findViewById(R.id.sleep_timer_spinner);
        repeatCounterTv = findViewById(R.id.repeat_counter_tv);
        playButton = findViewById(R.id.playid);
        pauseButton = findViewById(R.id.pauseid);
        stopButton = findViewById(R.id.stopid);
        playAnim = findViewById(R.id.playlottie);
        pauseAnim = findViewById(R.id.pauselottie);
        stopAnim = findViewById(R.id.stoplottie);
        toolbarTitle = findViewById(R.id.toolbar_title);
        reciterImage = findViewById(R.id.reciter_image);

        getData();
        setupSpinners();

        AppCompatImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        playButton.setOnClickListener(v -> startAudioServiceAction(AudioService.ACTION_PLAY));
        pauseButton.setOnClickListener(v -> startAudioServiceAction(AudioService.ACTION_PAUSE));
        stopButton.setOnClickListener(v -> startAudioServiceAction(AudioService.ACTION_STOP));

        updateUI(false, false);
    }

    private void getData() {
        reciterName = getIntent().getStringExtra("TITLE");
        if (reciterName == null) reciterName = "إسلام صبحي";
        reciterImageResId = getIntent().getIntExtra("RECITER_IMAGE", R.drawable.islam);
        mediaPlayerResId = getIntent().getIntExtra("MEDIA_PLAYER", R.raw.islam);

        toolbarTitle.setText(reciterName);
        reciterImage.setImageResource(reciterImageResId);
    }



    private void setupSpinners() {
        ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter.createFromResource(
                this, R.array.repeat_options, android.R.layout.simple_spinner_item);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(repeatAdapter);

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: selectedRepeatCount = -1; break;
                    case 1: selectedRepeatCount = 1; break;
                    case 2: selectedRepeatCount = 3; break;
                    case 3: selectedRepeatCount = 4; break;
                    case 4: selectedRepeatCount = 5; break;
                    case 5: selectedRepeatCount = 7; break;
                    case 6: selectedRepeatCount = 10; break;
                    case 7: selectedRepeatCount = 15; break;
                    case 8: selectedRepeatCount = 20; break;
                    case 9: selectedRepeatCount = 30; break;
                    case 10: selectedRepeatCount = 50; break;
                    case 11: selectedRepeatCount = 100; break;
                    default: selectedRepeatCount = -1; break;
                }

                if (isBound && audioService != null) {
                    audioService.setRepeatCount(selectedRepeatCount);
                }

                if (isInitialSpinnerSelection) {
                    isInitialSpinnerSelection = false;
                } else {
                    String selectedText = parent.getItemAtPosition(position).toString();
                    Toast.makeText(MediaPlayerActivity.this, "التكرار: " + selectedText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRepeatCount = -1;
            }
        });

        ArrayAdapter<CharSequence> sleepAdapter = ArrayAdapter.createFromResource(
                this, R.array.sleep_timer_options, android.R.layout.simple_spinner_item);
        sleepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sleepTimerSpinner.setAdapter(sleepAdapter);

        sleepTimerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long minutes = 0;
                switch (position) {
                    case 1: minutes = 15; break;
                    case 2: minutes = 30; break;
                    case 3: minutes = 45; break;
                    case 4: minutes = 60; break;
                    case 0:
                    default: minutes = 0; break;
                }

                if (isBound && audioService != null) {
                    audioService.setSleepTimer(minutes);
                }

                if (isInitialSleepSelection) {
                    isInitialSleepSelection = false;
                } else if (minutes > 0) {
                    Toast.makeText(MediaPlayerActivity.this, "سيتم إيقاف الصوت بعد " + minutes + " دقيقة", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void startAudioServiceAction(String action) {
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(action);
        serviceIntent.putExtra("TITLE", reciterName);
        serviceIntent.putExtra("RECITER_IMAGE", reciterImageResId);
        serviceIntent.putExtra("MEDIA_PLAYER", mediaPlayerResId);
        serviceIntent.putExtra("REPEAT_COUNT", selectedRepeatCount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && AudioService.ACTION_PLAY.equals(action)) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void updateUIFromService() {
        if (isBound && audioService != null) {
            updateUI(audioService.isPlaying(), audioService.isPaused());
            updateCounterText(audioService.getCompletedRepeats(), audioService.getRepeatCount());
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

    private void updateCounterText(int completed, int total) {
        if (repeatCounterTv != null) {
            if (total == -1) {
                repeatCounterTv.setText("تكرار مستمر - المكتمل: " + completed);
            } else {
                repeatCounterTv.setText("التكرار الحالي: " + completed + " من " + total);
            }
        }
    }

    @Override
    public void onPlaybackStateChanged(boolean playing, boolean paused, int completed, int total) {
        runOnUiThread(() -> {
            updateUI(playing, paused);
            updateCounterText(completed, total);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, AudioService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            if (audioService != null) {
                audioService.setCallback(null);
            }
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}
