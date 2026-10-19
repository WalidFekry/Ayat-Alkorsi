package com.walid.ayatalkorsi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AudioService extends Service {

    public static final String ACTION_PLAY = "com.walid.ayatalkorsi.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.walid.ayatalkorsi.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.walid.ayatalkorsi.ACTION_STOP";
    public static final String CHANNEL_ID = "audio_playback_channel";
    public static final int NOTIFICATION_ID = 2024;

    private final IBinder binder = new AudioBinder();
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;
    private boolean hasAudioFocus = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private String reciterTitle = "";
    private int reciterImageResId = R.drawable.islam;
    private int mediaPlayerResId = R.raw.islam;

    private int repeatCount = -1; // -1 for infinite, else target count
    private int completedRepeats = 0;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    private Runnable sleepTimerRunnable;
    private AudioServiceCallback callback;

    public interface AudioServiceCallback {
        void onPlaybackStateChanged(boolean playing, boolean paused, int completed, int total);
    }

    public class AudioBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = focusChange -> {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pauseAudio();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0.2f, 0.2f);
                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    if (isPaused) {
                        playAudio();
                    }
                }
                break;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                if (intent.hasExtra("TITLE")) {
                    reciterTitle = intent.getStringExtra("TITLE");
                    reciterImageResId = intent.getIntExtra("RECITER_IMAGE", R.drawable.islam);
                    int resId = intent.getIntExtra("MEDIA_PLAYER", R.raw.islam);
                    if (resId != mediaPlayerResId && mediaPlayer != null) {
                        stopAudio();
                    }
                    mediaPlayerResId = resId;
                }
                if (intent.hasExtra("REPEAT_COUNT")) {
                    repeatCount = intent.getIntExtra("REPEAT_COUNT", -1);
                }
                playAudio();
            } else if (ACTION_PAUSE.equals(action)) {
                pauseAudio();
            } else if (ACTION_STOP.equals(action)) {
                stopAudio();
                stopSelf();
            }
        }
        return START_NOT_STICKY;
    }

    public void setCallback(AudioServiceCallback callback) {
        this.callback = callback;
        notifyCallback();
    }

    public void setMediaData(String title, int imageResId, int mediaResId, int repeatCount) {
        if (this.mediaPlayerResId != mediaResId && isPlaying) {
            stopAudio();
        }
        this.reciterTitle = title;
        this.reciterImageResId = imageResId;
        this.mediaPlayerResId = mediaResId;
        this.repeatCount = repeatCount;
    }

    public void setRepeatCount(int count) {
        this.repeatCount = count;
        notifyCallback();
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getCompletedRepeats() {
        return completedRepeats;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public String getReciterTitle() {
        return reciterTitle;
    }

    public void playAudio() {
        if (requestAudioFocus()) {
            try {
                if (mediaPlayer == null) {
                    createAndPrepareMediaPlayer();
                }
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
                createAndPrepareMediaPlayer();
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            }

            isPlaying = true;
            isPaused = false;
            startForegroundServiceNotification();
            notifyCallback();
        }
    }

    private void createAndPrepareMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, mediaPlayerResId);
        if (mediaPlayer != null) {
            completedRepeats = 0;
            mediaPlayer.setOnCompletionListener(mp -> {
                completedRepeats++;
                notifyCallback();
                if (repeatCount == -1 || completedRepeats < repeatCount) {
                    mediaPlayer.start();
                    updateNotification();
                } else {
                    isPlaying = false;
                    isPaused = false;
                    notifyCallback();
                    updateNotification();
                    stopForegroundService(false);
                }
            });
        }
    }

    public void pauseAudio() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isPlaying = false;
        isPaused = true;
        abandonAudioFocus();
        notifyCallback();
        updateNotification();
        stopForegroundService(false);
    }

    public void stopAudio() {
        cancelSleepTimer();
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
        isPaused = false;
        completedRepeats = 0;
        abandonAudioFocus();
        notifyCallback();
        stopForegroundService(true);
    }

    public void setSleepTimer(long minutes) {
        cancelSleepTimer();
        if (minutes > 0) {
            sleepTimerRunnable = this::stopAudio;
            handler.postDelayed(sleepTimerRunnable, minutes * 60 * 1000);
        }
    }

    public void cancelSleepTimer() {
        if (sleepTimerRunnable != null) {
            handler.removeCallbacks(sleepTimerRunnable);
            sleepTimerRunnable = null;
        }
    }

    private void startForegroundServiceNotification() {
        Notification notification = buildNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
            } catch (Exception e) {
                startForeground(NOTIFICATION_ID, notification);
            }
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    private void stopForegroundService(boolean removeNotification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(removeNotification ? STOP_FOREGROUND_REMOVE : STOP_FOREGROUND_DETACH);
        } else {
            stopForeground(removeNotification);
        }
    }

    private void notifyCallback() {
        if (callback != null) {
            callback.onPlaybackStateChanged(isPlaying, isPaused, completedRepeats, repeatCount);
        }
    }

    private boolean requestAudioFocus() {
        if (hasAudioFocus) {
            return true;
        }
        int result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (audioFocusRequest == null) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(audioAttributes)
                        .setAcceptsDelayedFocusGain(false)
                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
                        .build();
            }
            result = audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        hasAudioFocus = (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
        return hasAudioFocus;
    }

    private void abandonAudioFocus() {
        if (audioManager != null && hasAudioFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest != null) {
                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            } else {
                audioManager.abandonAudioFocus(audioFocusChangeListener);
            }
            hasAudioFocus = false;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "تشغيل آية الكرسي",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("التحكم في تشغيل آية الكرسي في الخلفية");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void updateNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, buildNotification());
        }
    }

    private PendingIntent getServicePendingIntent(int requestCode, Intent intent, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return PendingIntent.getForegroundService(this, requestCode, intent, flags);
        } else {
            return PendingIntent.getService(this, requestCode, intent, flags);
        }
    }

    private Notification buildNotification() {
        Intent openIntent = new Intent(this, MediaPlayerActivity.class);
        openIntent.putExtra("TITLE", reciterTitle);
        openIntent.putExtra("RECITER_IMAGE", reciterImageResId);
        openIntent.putExtra("MEDIA_PLAYER", mediaPlayerResId);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, openIntent, flags);

        Intent playIntent = new Intent(this, AudioService.class);
        playIntent.setAction(ACTION_PLAY);
        playIntent.putExtra("TITLE", reciterTitle);
        playIntent.putExtra("RECITER_IMAGE", reciterImageResId);
        playIntent.putExtra("MEDIA_PLAYER", mediaPlayerResId);
        playIntent.putExtra("REPEAT_COUNT", repeatCount);
        PendingIntent playPendingIntent = getServicePendingIntent(1, playIntent, flags);

        Intent pauseIntent = new Intent(this, AudioService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pausePendingIntent = getServicePendingIntent(2, pauseIntent, flags);

        Intent stopIntent = new Intent(this, AudioService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = getServicePendingIntent(3, stopIntent, flags);

        String subtitle;
        if (repeatCount == -1) {
            subtitle = "تكرار مستمر - المكتمل: " + completedRepeats;
        } else {
            subtitle = "التكرار: " + completedRepeats + " من " + repeatCount;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(reciterTitle.isEmpty() ? "آية الكرسي مكررة" : reciterTitle)
                .setContentText(subtitle)
                .setContentIntent(contentPendingIntent)
                .setOngoing(isPlaying)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        if (isPlaying) {
            builder.addAction(android.R.drawable.ic_media_pause, "إيقاف مؤقت", pausePendingIntent);
        } else {
            builder.addAction(android.R.drawable.ic_media_play, "تشغيل", playPendingIntent);
        }
        builder.addAction(android.R.drawable.ic_delete, "إيقاف", stopPendingIntent);

        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        stopAudio();
        super.onDestroy();
    }
}
